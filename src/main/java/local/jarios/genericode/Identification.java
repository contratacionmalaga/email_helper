package local.jarios.genericode;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Identification")
public class Identification {
	
	@XmlElement(name = "ShortName")
	private String shortName;

	@XmlElement(name = "LongName")
	private String longName;

	@XmlElement(name = "Version")
	private String version;

	@XmlElement(name = "CanonicalUri")
	private String canonicalUri;

	@XmlElement(name = "CanonicalVersionUri")
	private String canonicalVersionUri;

	@XmlElement(name = "LocationUri")
	private String locationUri;

}
