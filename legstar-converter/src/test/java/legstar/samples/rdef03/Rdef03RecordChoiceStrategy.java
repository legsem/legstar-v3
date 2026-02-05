package legstar.samples.rdef03;

import java.lang.reflect.Field;

import org.legstar.cobol.converter.CobolConverterFromHostChoiceStrategy;

import legstar.samples.rdef03.Rdef03Record.ComDetail1Choice;

public class Rdef03RecordChoiceStrategy implements CobolConverterFromHostChoiceStrategy<Rdef03Record> {

	@Override
	public boolean choose(Rdef03Record rdef03Record, Object choice, Field alternative) {
		if (choice instanceof ComDetail1Choice) {
			switch (alternative.getName()) {
			case "comDetail1":
				return rdef03Record.getComSelect() == 0;
			case "comDetail2":
				return rdef03Record.getComSelect() == 1;
			case "comDetail3":
				return rdef03Record.getComSelect() == 2;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

}
