FROM swiftdoc/swift-doc:latest AS app-doc

COPY . /app
WORKDIR /app
RUN swift-doc generate . --module-name Caltrack --output doc/ --minimum-access-level internal --format html --base-url /var/www/html