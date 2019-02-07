From java
COPY . .
RUN java -version
CMD gradle test
