package scala
import java.io.IOException

import org.zeromq.ZMQ

object ZMQServiceSubscriber {
  var pushClient: ZMQ.Socket = _
  var pubClient: ZMQ.Socket = _
  var forwardMessage: ZMQ.Socket = _

  def init (pushPort: String, pubPort: String): Unit = {
    //creating ZMQ context which will be used for PUB and PUSH
    val context = ZMQ.context(1)

    //using context to create PUSH and PUB models and binding them to sockets
    pushClient = context.socket(ZMQ.PULL)
    pushClient.bind("tcp://127.0.0.1:"+ pushPort)

    pubClient = context.socket(ZMQ.SUB)
    pubClient.connect("tcp://127.0.0.1:"+ pubPort)
    pubClient.subscribe("Publisher".getBytes(ZMQ.CHARSET))

//    /**
//     * Below we are syncing pubServer with subscriber and print out received messages
//     */
//    // Synchronize with the publisher
//    val sync = context.socket(ZMQ.PUSH)
//
//    subscriber.setIdentity("Hello".getBytes)
//    subscriber.subscribe("".getBytes)
//    subscriber.connect("tcp://localhost:5565")
//    sync.connect("tcp://localhost:5564")
//    sync.send("".getBytes, 0)
//
//    // Get updates, expect random Ctrl-C death
//    var msg = ""
//    do {
//      msg = new String(subscriber.recv(0))
//      println(msg)
//    } while (!msg.equalsIgnoreCase("END"))
  }

  def init (pushPort: String, pubPort: String, forwardPort: String): Unit = {
    //creating ZMQ context which will be used for PUB and PUSH
    val context = ZMQ.context(1)

    //using context to create PUSH and PUB models and binding them to sockets
    pushClient = context.socket(ZMQ.PULL)
    pushClient.bind("tcp://127.0.0.1:"+ pushPort)

    pubClient = context.socket(ZMQ.SUB)
    pubClient.connect("tcp://127.0.0.1:"+ pubPort)
    pubClient.subscribe("Publisher".getBytes(ZMQ.CHARSET))

    forwardMessage = context.socket(ZMQ.PUB)
    forwardMessage.bind("tcp://127.0.0.1:"+ forwardPort)
    //    /**
    //     * Below we are syncing pubServer with subscriber and print out received messages
    //     */
    //    // Synchronize with the publisher
    //    val sync = context.socket(ZMQ.PUSH)
    //
    //    subscriber.setIdentity("Hello".getBytes)
    //    subscriber.subscribe("".getBytes)
    //    subscriber.connect("tcp://localhost:5565")
    //    sync.connect("tcp://localhost:5564")
    //    sync.send("".getBytes, 0)
    //
    //    // Get updates, expect random Ctrl-C death
    //    var msg = ""
    //    do {
    //      msg = new String(subscriber.recv(0))
    //      println(msg)
    //    } while (!msg.equalsIgnoreCase("END"))
  }

  def pull (): Unit = {
    val message = pushClient.recvStr()
    println("Push/Pull Message: " + message + " Topic: " + Thread.currentThread().getName)
  }

  def sub (): Unit = {
    println("entered sub")
    var topic: String = null
    try {
      topic = pubClient.recvStr()
    } catch {
      case e: Exception => e.printStackTrace()
      case e: IOException => {
        e.printStackTrace()
        e.toString()
      }
    }
    println(topic+": is topic")
    val message = pubClient.recvStr()
    println(message+": is message")
    println("Topic: " + topic + " Pub/Sub Message: " + message)
  }

  def forward (): Unit = {
    val topic = pubClient.recvStr()
    val message = pubClient.recvStr()
    println("Topic: " + topic + " Pub/Sub Message: " + message)
    /** publish the message again on forwardPort
     *
     */
    val pubMessage = message
    //setting the topic as Publisher
    forwardMessage.sendMore(topic)
    //sending the message
    val msg = String.format("Update %d" + pubMessage)
    forwardMessage.send(msg.getBytes(), 0)
    println(msg)
    Thread sleep 1000
  }
}
