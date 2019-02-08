From java
COPY . .
CMD gradle build
RUN gradle test
