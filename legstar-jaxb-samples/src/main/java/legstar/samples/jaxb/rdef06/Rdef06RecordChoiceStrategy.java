package legstar.samples.jaxb.rdef06;

import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolFieldInfo;

import legstar.samples.jaxb.rdef06.Rdef06Record.OptlItemChoice;


public class Rdef06RecordChoiceStrategy implements CobolChoiceStrategy<Rdef06Record> {

	@Override
	public boolean choose(Rdef06Record rdef04Record, Object choice, CobolFieldInfo alternative) {
		if (choice instanceof OptlItemChoice) {
			switch (alternative.name()) {
			case "optlStruct":
				return true;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

}
