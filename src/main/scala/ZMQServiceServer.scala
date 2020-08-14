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
    pushSocket.bind("tcp://127.0.0.1:"+ pushPort)

    pubSocket = context.socket(ZMQ.PUB)
    pubSocket.bind("tcp://127.0.0.1:"+ pubPort)

    /**
     * Below we are syncing pubServer with subscriber
     */
//    // Subscriber tells us when it's ready here
//    val sync = context.socket(ZMQ.PULL)
//    sync.bind("tcp://127.0.0.1:5564")
//    publisher setHWM 2
//    // Wait for synchronization request
//    sync recv 0
  }

  def push (message: Int): Unit = {
    /**
     * Below we are setting a topic and pushing the data
     */
    var pushMessage = message
      //setting the topic as Push Server
      //pushServer.sendMore("Raw Data Pushed")
      //sending the message
      val msg = String.format("Update %d" + pushMessage)
      pushSocket.send(msg.getBytes(), 0)
      println(msg)
  }

  def pub (message: Int): Unit = {
    /**
     * Below we are setting a topic and publishing the data
     */
    var pubMessage = message
    //setting the topic as Publisher
    pubSocket.sendMore("Publisher")
    //sending the message
    val msg = String.format("Update %d" + pubMessage)
    pubSocket.send(msg.getBytes(), 0)
    println(msg)
  }

  def end (): Unit = {
    context.close()
  }
}
