FROM thomasleplus/xml AS app-build

COPY . /build
WORKDIR /build
RUN xmllint --noout --dtdvalid schema/schema.dtd data.xml
RUN xmllint --noout --dtdvalid schema/schema.dtd data/*.xml
RUN xmllint --noout --relaxng schema/schema.rng data.xml
RUN java -jar libs/bin/saxon.jar -o:html/output.html -s:data.xml -xsl:xslt/html.xslt
RUN java -jar libs/bin/saxon.jar -o:pdf/output.fo -s:data.xml -xsl:xslt/pdf.xslt
RUN libs/fop -fo pdf/output.fo -pdf pdf/output.pdf