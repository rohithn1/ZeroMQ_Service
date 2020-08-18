package scala
import org.zeromq.ZMQ

object ZMQServiceServer {
  var pushSocket: ZMQ.Socket = _
  var pubSocket: ZMQ.Socket = _
  var context: ZMQ.Context = _

  def init (pushPort: String, pubPort: String): Unit = {
    //creating ZMQ context which will be used for PUB and PUSH
    context = ZMQ.context(1)
    //using context to create PUSH and PUB models and binding them to sockets
    pushSocket = context.socket(ZMQ.PUSH)
    pushSocket.connect("tcp://127.0.0.1:"+ pushPort)

    pubSocket = context.socket(ZMQ.PUB)
    pubSocket.bind("tcp://127.0.0.1:"+ pubPort)
  }

  def push (message: Int): Unit = {
    /**
     * Below we are setting a topic and pushing the data
     */
      val msg = "Update: " + message
      pushSocket.send(msg.getBytes(), 0)
      println("Pushed: " + msg)
  }

  def pub (message: Int): Unit = {
    /**
     * Below we are setting a topic and publishing the data
     */
    //setting the topic as Publisher
    pubSocket.sendMore("Publisher")
    //sending the message
    val msg = "Update: " + message
    pubSocket.send(msg.getBytes(ZMQ.CHARSET), 0)
    println("Published: " + msg)
  }

  def end (): Unit = {
    pushSocket.close()
    pubSocket.close()
  }
}
