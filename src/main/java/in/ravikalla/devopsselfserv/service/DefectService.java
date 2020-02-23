package in.ravikalla.devopsselfserv.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefectService {
	Logger L = LoggerFactory.getLogger(DefectService.class);

	public Issue create(String strToken, String strUserName, String strRepoName, String strTitle, String strLabel, String strBody) throws IOException {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(strToken);

		Issue issue = null;

		try {
			issue = createDefect(strUserName, strRepoName, strTitle, strBody, strLabel, client);
		} catch (IOException e) {
			L.error("26 : DefectService.create(...) : IOException e = {}", e);
			throw e;
		}
		return issue;
	}

	public List<Issue> get(String strToken, String strUserName, String strRepoName) throws IOException {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(strToken);
		IssueService iService = new IssueService(client); // TODO : Optimize this by creating a Spring bean
		List<Issue> lstDefects = null;

		try {
			lstDefects = getDefects(strUserName, strRepoName, iService);
		} catch (IOException e) {
			L.error("39 : DefectService.get(...) : IOException e = {}", e);
			throw e;
		}
		return lstDefects;
	}

	private Issue createDefect(String strUser, String strRepo, String strTitle, String strBody, String strLabel, GitHubClient client) throws IOException {
		IssueService issueService = new IssueService(client);
	
		Issue issue = new Issue();
		issue.setTitle(strTitle);
		issue.setBody(strBody);
	//	issue.setAssignee(userService.getUser("ravikalla"));
		List<Label> labels = new ArrayList<Label>();
		Label label = new Label();
		label.setName(strLabel);
		labels.add(label);
		issue.setLabels(labels);
	
		return issueService.createIssue(strUser, strRepo, issue);
	}
	
	private List<Issue> getDefects(String strUser, String strRepo, IssueService iService) throws IOException {
		Map<String, String> filderdata = new HashMap<String, String>();
		filderdata.put(IssueService.FILTER_LABELS, "enhancement");
		filderdata.put(IssueService.FILTER_STATE, IssueService.STATE_OPEN);
		List<Issue> lstDefects = iService.getIssues(strUser, strRepo, filderdata);
		lstDefects.addAll(iService.getIssues(strUser, strRepo, filderdata));
	
		return lstDefects;
	}
}
