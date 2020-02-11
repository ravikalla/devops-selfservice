package in.ravikalla.githubapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

public class FetchSoftwareRequirementReviewMeeting {

	private FetchSoftwareRequirementReviewMeeting(String strToken) {
		client = new GitHubClient();
		client.setOAuth2Token(strToken);
		iService = new IssueService(client);
	}

	private GitHubClient client = null;
	private IssueService iService = null;

	public static void main(String[] args) {

		if (args.length >= 6) {
			String strToken = args[0];
			String strUserName = args[1];
			String strRepoName = args[2];
			String strTitle = args[3];
			String strBody = args[4];
			String strLabel = args[5];

			List<Issue> loadRequirements;
			try {
				FetchSoftwareRequirementReviewMeeting defectApi = new FetchSoftwareRequirementReviewMeeting(strToken);

				defectApi.createDefect(strUserName, strRepoName, strTitle, strBody, strLabel);

				loadRequirements = defectApi.loadRequirements(strUserName, strRepoName);
				System.out.println("42 : Size = " + loadRequirements.size());
				for (Issue issue : loadRequirements) {
					System.out.println("44 : issue = " + issue.getTitle() + " : " + issue.getBody() + " : " + issue.getUrl() + " : " + issue.getAssignee() + " : " + issue.getCreatedAt());
				}
			} catch (IOException e) {
				System.out.println("47 : FetchSoftwareRequirementReviewMeeting.main(...) : " + e);
			}
		}
		else {
			System.out.println("Pass 4 arguments : <Token> <Issue Title> <Issue Body> <Label>");
		}
	}

	private void createDefect(String strUser, String strRepo, String strTitle, String strBody, String strLabel) throws IOException {
		IssueService issueService = new IssueService(client);

		Issue issue = new Issue();
		issue.setTitle(strTitle);
		issue.setBody(strBody);
//		issue.setAssignee(userService.getUser("ravikalla"));
		List<Label> labels = new ArrayList<Label>();
		Label label = new Label();
		label.setName(strLabel);
		labels.add(label);
		issue.setLabels(labels);

		issueService.createIssue(strUser, strRepo, issue);
	}

	private List<Issue> loadRequirements(String strUser, String strRepo) throws IOException {
		Map<String, String> filderdata = new HashMap<String, String>();
		filderdata.put(IssueService.FILTER_LABELS, "enhancement");
		filderdata.put(IssueService.FILTER_STATE, IssueService.STATE_OPEN);
		List<Issue> issues = iService.getIssues(strUser, strRepo, filderdata);
		issues.addAll(iService.getIssues(strUser, strRepo, filderdata));

		return issues;
	}
}