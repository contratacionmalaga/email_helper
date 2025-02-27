package local.jarios.genericode;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Value")
public class Value {
	
	@XmlElement(name = "SimpleValue")
	private String simpleValue;
	
	@XmlAttribute(name="ColumnRef")
	protected String columnRef;

}
