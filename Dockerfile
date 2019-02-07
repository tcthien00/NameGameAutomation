From java
COPY . .
RUN java -version
CMD gradle build
CMD gradle test
