package scala

object ZMQServiceTester {
  def main (args: Array[String]): Unit = {
    ZMQServiceServer.init("5000", "6000") //pushPort: 5000, //pubPort: 6000
    ZMQServiceSubscriber.init("5000", "6000")
    println("Initialized both aspects")

    var msg = 0
    while (msg < 10) {
      ZMQServiceServer.push(msg)
      ZMQServiceServer.pub(msg)
      println("Server Complete")
      ZMQServiceSubscriber.pull()
      ZMQServiceSubscriber.sub()
      msg = msg+1
      Thread sleep 1000
    }
    ZMQServiceServer.end()
    println("the project ended")
  }
}
