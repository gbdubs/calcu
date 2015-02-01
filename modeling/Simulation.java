import java.util.ArrayList;
import java.util.List;


public class Simulation {

	public static void main(String[] args){
		long l = System.currentTimeMillis();
		List<Problem> problems = new ArrayList<Problem>();
		List<User> users = new ArrayList<User>();
		for(int i = 0; i < 10; i++){
			problems.add(new Problem());
		}
		users.add(new User(problems));
		users.add(new User(problems));
		
		while(System.currentTimeMillis() - l < 10000){
			if (System.currentTimeMillis() % 100 == 0 && problems.size() < 200){
				problems.add(new Problem());
			}
			if (System.currentTimeMillis() % 80 == 0){
				User u = new User(problems);
				u.start();
			}
			
		}
		for (User u : users){
			try {
				u.interrupt();
				u.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<List<Integer>> errors = new ArrayList<List<Integer>>();
		for (Problem p : problems){
			System.out.println(p.toString());
			List<Integer> rErrors = p.rError;
			for(int i = 0; i < rErrors.size(); i++){
				if (errors.size() <= i){
					errors.add(new ArrayList<Integer>());
				}
				errors.get(i).add(rErrors.get(i));
			}
		}
		for(int i = 0; i < errors.size(); i++){
			Statistics stat = new Statistics(errors.get(i));
			
			System.out.println(i+" Mean="+ stat.getMean() + " SD=" + stat.getStdDev());
		}
	}
}
