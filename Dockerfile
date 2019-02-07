From ubuntu
COPY . .
RUN java -version
CMD gradle test
