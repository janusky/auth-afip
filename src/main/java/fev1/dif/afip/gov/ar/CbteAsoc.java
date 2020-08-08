
package fev1.dif.afip.gov.ar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CbteAsoc complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CbteAsoc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tipo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Nro" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Cuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CbteFch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CbteAsoc", propOrder = {
    "tipo",
    "ptoVta",
    "nro",
    "cuit",
    "cbteFch"
})
public class CbteAsoc {

    @XmlElement(name = "Tipo")
    protected int tipo;
    @XmlElement(name = "PtoVta")
    protected int ptoVta;
    @XmlElement(name = "Nro")
    protected long nro;
    @XmlElement(name = "Cuit")
    protected String cuit;
    @XmlElement(name = "CbteFch")
    protected String cbteFch;

    /**
     * Obtiene el valor de la propiedad tipo.
     * 
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Define el valor de la propiedad tipo.
     * 
     */
    public void setTipo(int value) {
        this.tipo = value;
    }

    /**
     * Obtiene el valor de la propiedad ptoVta.
     * 
     */
    public int getPtoVta() {
        return ptoVta;
    }

    /**
     * Define el valor de la propiedad ptoVta.
     * 
     */
    public void setPtoVta(int value) {
        this.ptoVta = value;
    }

    /**
     * Obtiene el valor de la propiedad nro.
     * 
     */
    public long getNro() {
        return nro;
    }

    /**
     * Define el valor de la propiedad nro.
     * 
     */
    public void setNro(long value) {
        this.nro = value;
    }

    /**
     * Obtiene el valor de la propiedad cuit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuit() {
        return cuit;
    }

    /**
     * Define el valor de la propiedad cuit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuit(String value) {
        this.cuit = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteFch.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCbteFch() {
        return cbteFch;
    }

    /**
     * Define el valor de la propiedad cbteFch.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCbteFch(String value) {
        this.cbteFch = value;
    }

}
