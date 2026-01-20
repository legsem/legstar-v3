package legstar.samples.rdef03;

import java.lang.reflect.Field;

import org.legstar.cobol.type.converter.CobolConverterFromHostChoiceStrategy;

public class Rdef03ConverterChoiceStrategy implements CobolConverterFromHostChoiceStrategy<Rdef03Record> {

	@Override
	public boolean choose(Rdef03Record rdef03Record, Field alternative) {
		switch (rdef03Record.getComSelect()) {
		case 0:
			return (alternative.getName().equals("comDetail1"));
		case 1:
			return (alternative.getName().equals("comDetail2"));
		case 2:
			return (alternative.getName().equals("comDetail3"));
		default:
			return false;
		}
	}

}
