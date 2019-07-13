package in.sskrishna.convoy;

import in.sskrishna.convoy.service.core.StartupService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class ConvoyApplication {

    public static void main(String[] args) throws IOException, GitAPIException {
        SpringApplication.run(ConvoyApplication.class, args);
//		test();
    }

	@Bean
	public ApplicationRunner applicationRunner(StartupService startupService){
		return (args)->{
			startupService.startUp();
		};
	}

//	public static void test() throws IOException, GitAPIException {
//		Repository repoInterface = new FileRepository("/tmp/convoy/jgit");
//		Git git = new Git(repoInterface);
//
//		Iterable<RevCommit> commitItr = git.log().add(repoInterface.resolve("refs/heads/" + "master")).call();
//		commitItr.forEach((commitRef) -> {
//			PersonIdent pi = commitRef.getAuthorIdent();
//			Commit.Person person = new Commit.Person();
//
//			person.setName(pi.getName());
//			person.setEmail(pi.getEmailAddress());
//
//			ZoneId zoneId = pi.getTimeZone().getDefault().toZoneId();
//			LocalDateTime ldt = LocalDateTime.ofInstant(pi.getWhen().toInstant(),
//					zoneId);
//
//			person.setTime(ldt);
//		});
//	}

}