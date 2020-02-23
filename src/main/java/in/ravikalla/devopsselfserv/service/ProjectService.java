package in.ravikalla.devopsselfserv.service;

import java.io.File;
import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
	Logger L = LoggerFactory.getLogger(ProjectService.class);

	public Repository create(String strOrg, String strToken, String strRepoName, Boolean blnIsPrivate, String strCloneURL) throws IOException {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(strToken);
		RepositoryService repositoryService = new RepositoryService(client); // TODO : Optimize this by creating a Spring bean

		Repository repository  = null;
		try {
			repository = createProject(strOrg, strRepoName, blnIsPrivate, strCloneURL, repositoryService);
		} catch (IOException e) {
			L.error("29 : ProjectService.create(...) : IOException e = {}", e);
			throw e;
		}
		return repository;
	}

	public Repository gitFork(String strOrg, String strToken, String strRepoName, String strNewOrg) throws IOException {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(strToken);
		RepositoryService repositoryService = new RepositoryService(client); // TODO : Optimize this by creating a Spring bean

		Repository repository  = null;
		try {
			repository = forkProject(strOrg, strRepoName, strNewOrg, repositoryService);
		} catch (IOException e) {
			L.error("44 : ProjectService.fork(...) : IOException e = {}", e);
			throw e;
		}
		return repository;
	}

	private Repository createProject(String strOrg, String strRepoName, Boolean blnIsPrivate, String strCloneURL, RepositoryService repositoryService) throws IOException {
		Repository repository = new Repository();
		repository.setName(strRepoName);
		repository.setPrivate(blnIsPrivate);
		repository.setCloneUrl(strCloneURL);
		Repository createRepository = repositoryService.createRepository(strOrg, repository);
		return createRepository;
	}

	private Repository forkProject(String strOrg, String strRepoName, String strNewOrg, RepositoryService repositoryService) throws IOException {
		RepositoryId repo = new RepositoryId(strOrg, strRepoName);
		Repository createRepository = repositoryService.forkRepository(repo, strNewOrg);
		return createRepository;
	}

	public void cloneRepo(String owner, String repoName, RepositoryService rs, String LOCAL_TEMP_PATH)
			throws Exception {
		Git result = null;
		try {
			Repository r = rs.getRepository(owner, repoName);

			String cloneURL = r.getSshUrl();
			// prepare a new folder for the cloned repository
			File localPath = new File(LOCAL_TEMP_PATH + File.separator + owner + File.separator + repoName);
			if (localPath.isDirectory() == false) {
				localPath.mkdirs();
			} else {
				throw new Exception("Local directory already exists. Delete it first: " + localPath);
			}

			L.debug("Cloning from " + cloneURL + " to " + localPath);
			result = Git.cloneRepository().setURI(cloneURL).setDirectory(localPath).call();
			// Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
			L.debug("Cloned repository: " + result.getRepository().getDirectory());
		} catch (IOException | GitAPIException ex) {
			throw new Exception("Problem cloning repo: " + ex.getMessage());
		} finally {
			if (result != null) {
				result.close();
			}
		}
	}
}
