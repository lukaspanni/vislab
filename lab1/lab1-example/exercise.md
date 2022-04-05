#1.a

Describe the characteristics of this distributed system:

- Which interaction style (synchrony, invocation semantics) is used in the
communication between the test client and your Web service?
  - synchronous communication
  - payload semantics -> the soap-message describes the action that should be executed

- Describe the activities of the system components during the communication
process.
  - Components: _FibonacciService_ on both sides
    - Server: _FibonacciServiceImpl_ published through _javax.xml.ws.Endpoint_
    - Client: interface implementation generated from wsdl
  
  - Activities: The Client calls method on generated client-object, object translates method-call to a SOAP-Message and sends it to the endpoint. 
  The server receives it and converts the SOAP-Message to a method-call on the service-implementation-object.
  The call-result is then converted back to a SOAP-Message and sent to the client which extracts the result.

#1.b

Get the WSDL description of the Fibonacci Web Service using a Web browser (run TestWsServer.java, GET the Web service endpoint with query param 'wsdl') and describe its
characteristics.

- What kind of binding was used?
  - soap-binding using http transport
  
- Which communication style was used for the specific binding?
  - document-style
  
- What kind of message encoding was used?
  - literal