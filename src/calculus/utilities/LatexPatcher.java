package calculus.utilities;


public class LatexPatcher {

	private static String[] removals = {"\\vc"};
	
	private static String[][] replacements =
		{
			{"\\R ", "\\mathbb R"},
			{"\\D ", "\\mathbb D"},
			{"\\Z ", "\\mathbb Z"},
			{"\\N ", "\\mathbb N"},
			{"\\sto", "\\rightarrow"}
		};
	
	private static String[][][] wrappedReplacements =
		{
			{{"\\emph{", "}"}, {"<i>", "<\\i>"}},
			{{"\\abs{", "}"}, {" \\left | ", "\\right | "}}
		};
	
	public static String makeReplacements(String s){
		for (String removal : removals){
			s = s.replace(removal, "");
		}
		for (String[] replacement : replacements){
			s = s.replace(replacement[0], replacement[1]);
		}
		for (String[][] wrappedReplacement : wrappedReplacements){
			int startIndex = s.indexOf(wrappedReplacement[0][0]);
			while (startIndex != -1){
				int endIndex = s.indexOf(wrappedReplacement[0][1], startIndex);
				String insides = s.substring(startIndex + wrappedReplacement[0][0].length(), endIndex);
				s = s.substring(0, startIndex) + wrappedReplacement[1][0] + insides + wrappedReplacement[1][1] +  s.substring(endIndex + wrappedReplacement[0][1].length());
				startIndex = s.indexOf(wrappedReplacement[0][0]);
			}
		}
		return s;
	}
	
	
}
