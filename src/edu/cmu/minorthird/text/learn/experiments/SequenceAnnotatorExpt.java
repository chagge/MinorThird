package edu.cmu.minorthird.text.learn.experiments;

import edu.cmu.minorthird.classify.Splitter;
import edu.cmu.minorthird.classify.sequential.CrossValidatedSequenceDataset;
import edu.cmu.minorthird.classify.sequential.SequenceClassifierLearner;
import edu.cmu.minorthird.classify.sequential.SequenceDataset;
import edu.cmu.minorthird.classify.experiments.*;
import edu.cmu.minorthird.text.Annotator;
import edu.cmu.minorthird.text.FancyLoader;
import edu.cmu.minorthird.text.TextEnv;
import edu.cmu.minorthird.text.learn.*;
import edu.cmu.minorthird.util.gui.ViewerFrame;
import edu.cmu.minorthird.util.gui.Visible;

/** Run an annotation-learning experiment based on a pre-labeled text
 * environment, using a sequence learning method, and showing the
 * result of evaluation of the sequence-classification level.
 *
 * @author William Cohen
*/

public class SequenceAnnotatorExpt
{
	private TextEnv env;
	private Splitter splitter;
	private SequenceClassifierLearner learner;
	private String inputLabel;
	private String tokPropFeats;
	private SequenceDataset sequenceDataset;

	public SequenceAnnotatorExpt(TextEnv env,Splitter splitter,SequenceClassifierLearner learner,String inputLabel)
	{
		this(env,splitter,learner,inputLabel,null);
	}

	public 
	SequenceAnnotatorExpt(
		TextEnv env,Splitter splitter,SequenceClassifierLearner learner,String inputLabel,String tokPropFeats)
	{
		this.env = env;
		this.splitter = splitter;
		this.learner = learner;
		this.inputLabel = inputLabel;
		this.tokPropFeats = tokPropFeats;
		AnnotatorTeacher teacher = new TextEnvAnnotatorTeacher(env,inputLabel);
		SampleFE.ExtractionFE fe = new SampleFE.ExtractionFE(3);
		if (tokPropFeats!=null) fe.setTokenPropertyFeatures(tokPropFeats);
		SequenceAnnotatorLearner dummy = new SequenceAnnotatorLearner(fe,3) {
				public Annotator getAnnotator() { return null; }
			};
		teacher.train(dummy);
		sequenceDataset = dummy.getSequenceDataset();
	}

	public CrossValidatedSequenceDataset crossValidatedDataset()
	{
		return new CrossValidatedSequenceDataset( learner, sequenceDataset, splitter );
	}

	public Evaluation evaluation()
	{
		Evaluation e = Tester.evaluate( learner, sequenceDataset, splitter );
		return e;
	}

	static public SequenceClassifierLearner toSeqLearner(String learnerName)
	{
		try {
			bsh.Interpreter interp = new bsh.Interpreter();
			interp.eval("import edu.cmu.minorthird.classify.*;");
			interp.eval("import edu.cmu.minorthird.classify.experiments.*;");
			interp.eval("import edu.cmu.minorthird.classify.algorithms.linear.*;");
			interp.eval("import edu.cmu.minorthird.classify.algorithms.trees.*;");
			interp.eval("import edu.cmu.minorthird.classify.algorithms.knn.*;");
			interp.eval("import edu.cmu.minorthird.classify.algorithms.svm.*;");
			interp.eval("import edu.cmu.minorthird.classify.sequential.*;");
			return (SequenceClassifierLearner)interp.eval(learnerName);
		} catch (bsh.EvalError e) {
			throw new IllegalArgumentException("error parsing learnerName '"+learnerName+"':\n"+e);
		}
	}

	public static void main(String[] args) 
	{
		TextEnv env=null;
		Splitter splitter=new RandomSplitter();
		SequenceClassifierLearner learner=null;
		String inputLabel=null;
		String tokPropFeats=null;
		String toShow = "eval";
		try {
			int pos = 0;
			while (pos<args.length) {
				String opt = args[pos++];
				if (opt.startsWith("-e")) {
					env = FancyLoader.loadTextEnv(args[pos++]);
				} else if (opt.startsWith("-sp")) {
					splitter = Expt.toSplitter(args[pos++]);
				} else if (opt.startsWith("-l")) {
					learner = toSeqLearner(args[pos++]);
				} else if (opt.startsWith("-i")) {
					inputLabel = args[pos++];
				} else if (opt.startsWith("-p")) {
					tokPropFeats = args[pos++];
				} else if (opt.startsWith("-sh")) {
					toShow = args[pos++];
				} else {
					usage();
				}
			}
			if (env==null || learner==null || splitter==null|| inputLabel==null) usage();
			SequenceAnnotatorExpt expt = new SequenceAnnotatorExpt(env,splitter,learner,inputLabel,tokPropFeats);
			Visible v = null; 
			if (toShow.startsWith("ev")) v = expt.evaluation();
			else if (toShow.startsWith("all")) v = expt.crossValidatedDataset();
			else usage(); 
			ViewerFrame f = new ViewerFrame("Evaluation",v.toGUI());
		} catch (Exception e) {
			e.printStackTrace();
			usage();
		}
	}
	private static void usage() {
		System.out.println("usage: -env envKey -learn learner -input inputLabel -split splitter -show all|eval");
	}
}
