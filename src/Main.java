import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import domain.PullRequest;
import domain.SimpleRepo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static final String GITHUB_API_BASE_URI = "https://api.github.com";
    static final String RAMDA = "ramda";
    static Client client = ClientBuilder.newClient();

    public static void main(String[] args) {
        List<SimpleRepo> ramdaRepos = getRepos(RAMDA);
        Multimap<String, PullRequest> pullRequestsByRepo = MultimapBuilder.hashKeys().arrayListValues().build();
        int totalPullRequests = 0;
        for (SimpleRepo repo : ramdaRepos) {
            String repoName = repo.getName();
            List<PullRequest> pullRequests = getPullRequests(RAMDA, repoName);
            pullRequestsByRepo.putAll(repoName, pullRequests);
            System.out.println("Repo: " + repoName + ", Pull requests: " + pullRequests.size());
            totalPullRequests += pullRequests.size();
        }
        System.out.println("Total pull requests: " + totalPullRequests);
    }

    static List<SimpleRepo> getRepos(String org) {
        return client.target(GITHUB_API_BASE_URI + "/orgs/" + org + "/repos")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SimpleRepo>>() {});
    }

    static List<PullRequest> getPullRequests(String org, String repo) {
        return client.target(GITHUB_API_BASE_URI + "/repos/" + org + "/" + repo + "/pulls")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<PullRequest>>() {});
    }
}
