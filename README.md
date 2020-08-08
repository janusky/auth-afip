# auth-afip

Aplicación con lo necesario para Autenticarse con AFIP. Utilizando el mecanismo llamado [WSAA](https://www.afip.gob.ar/ws/documentacion/wsaa.asp) (Webservice de Autenticación y Autorización). 

## Requerimiento

Conectarse al servicio de Factura Electrónica ([wsfev1](https://servicios1.afip.gov.ar/wsfev1/service.asmx)) en entorno de producción.

1. [Certificado para producción](#Crear-certificado-para-producción)

2. [Asociar certificado con Factura Electrónica](#Asociar-certificado-con-Factura-Electrónica)

>IMPORTANTE  
> Quien realiza los pasos debe contar con el acceso adecuado para ambas operaciones.

[Más información](https://www.afip.gob.ar/ws/documentacion/arquitectura-general.asp)

### Crear certificado para producción

* [Obtener Certificado](docs/WSAA.ObtenerCertificado.pdf)

Generar CSR para crear certificado de autenticación

```sh
# Acceder donde guardar archivos privados
cd auth-afip/certs/private

# Generación de privada
sudo openssl genrsa -out privkey.pem 2048

# Generación de requerimiento del certificado en PKCS#10
# -config ../openssl.cnf
# Indicar CUIT de quien utiliza WSFEv1
sudo openssl req -new -key privkey.pem \
  -subj "/C=AR/O=facturaws/CN=facturaws/serialNumber=CUIT 33693450239" \
  -out cert.csr
```

Acceder a https://www.afip.gob.ar y ingresar en [Administración de Certificados Digitales](docs/wsaa_obtener_certificado_produccion.pdf) para crear su Certificado X509 (`facturaws.crt`). Debe utilizar el certificado recién creado `cert.csr`.

Luego de finalizar la creación descargar el archivo `.crt` que tendrá el nombre indicado como alias `facturaws` (`facturaws.crt`). 

### Asociar certificado con Factura Electrónica  

* [Asociar Certificado con Servicio de Negocio](docs/ADMINREL.DelegarWS.pdf) 

Acceder a https://www.afip.gob.ar y ingresar en [Administrador de Relaciones](docs/GT_Administrador.pdf), crear `Nueva Relación` con Web Service de Factura Electrónica.

Luego de asociar el certificado con el servicio de Factura Electrónica, debe crear un archivo tipo PKCS#12 (utilizado en aplicación [auth-afip](#auth-afip)).

```sh
# Acceder donde se encuentra la clave privada (privkey.pem)
cd auth-afip/certs/private

# Convertir el archivo recién creado a P12, para luego utilizar en aplicación.
openssl pkcs12 -export -inkey privkey.pem -in facturaws.crt -out cert-prod.p12 -name 'facturaws'
```

## Uso

Descargar el proyecto listo para trabajar

```sh
git clone https://github.com/janusky/auth-afip.git
```

### Generar fuentes desde WSDL

Si se necesita generar los fuentes a partir del [WSDL wsfev1](https://servicios1.afip.gov.ar/wsfev1/service.asmx?WSDL)

Una vez importado el proyecto [auth-afip](https://github.com/janusky/auth-afip), debe ejecutar `wsimport` para generar las clases a partir del [WSDL](https://servicios1.afip.gov.ar/wsfev1/service.asmx?WSDL) del Web Service de Factura Electrónica ([wsfev1](https://servicios1.afip.gov.ar/wsfev1/service.asmx)).

El IDE debe contar con el **plugin/conector** [jaxws-maven-plugin](https://www.mojohaus.org/jaxws-maven-plugin/) que permite generar automáticamente los fuentes `Java` del servicio a partir del `WSDL` indicado.

Si el IDE utilizado no puede resolver el plugin `jaxws-maven-plugin` debe ejecutar por consola

>NOTA  
>Para salvar posible error porque el IDE no resuelve el plugin `jaxws:wsimport`, se incorpora excepción en `pluginManagement` 
>```
>Plugin execution not covered by lifecycle configuration: org.jvnet.jax-ws-commons:jaxws-maven-plugin:2.2:wsimport (Maven Project Build >Lifecycle Mapping Problem)
>```

```sh
# Generar Fuentes Java del WSDL (jaxws:wsimport)
mvn clean generate-sources
```

### Pruebas 

**Autenticación**

* Ejecutar `auth.AuthenticationServiceTest.java`

**Invocar servicio**

>NOTA: Requiere **token** y **sign** que debe copiar de la autenticación. 

* Servicio `FECompConsultar` con clase `fev1.dif.afip.gov.ar.FECompConsultarTest.java`

## Referencias

- <https://www.afip.gob.ar/ws/documentacion/arquitectura-general.asp>

- <https://www.afip.gob.ar/ws/documentacion/certificados.asp>

  - <http://www.afip.gob.ar/ws/WSAA/WSAA.ObtenerCertificado.pdf>
  
  - <http://www.afip.gob.ar/ws/WSAA/ADMINREL.DelegarWS.pdf>
  
  - <https://www.afip.gob.ar/ws/WSAA/wsaa_obtener_certificado_produccion.pdf>
  
  - <https://www.afip.gob.ar/ws/WSAA/wsaa_asociar_certificado_a_wsn_produccion.pdf>

- <https://www.afip.gob.ar/ws/WSASS/WSASS_manual.pdf>

- <http://www.afip.gov.ar/ws/WSAA/Especificacion_Tecnica_WSAA_1.2.0.pdf>

- <http://www.afip.gov.ar/ws/WSAA/README.txt>

- <http://www.afip.gob.ar/ws/WSAA/WSAAmanualDev.pdf>

- <https://serviciosweb.afip.gob.ar/genericos/guiaDeTramites/VerGuia.aspx?tr=19>

- <http://exgetmessage.blogspot.com/2018/02/conectar-tu-aplicacion-con-afip-sin.html>
