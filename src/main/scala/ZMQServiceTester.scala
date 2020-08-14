package scala

object ZMQServiceTester {
  def main (args: Array[String]): Unit = {
    ZMQServiceServer.init("5000", "6000") //pushPort: 5000, //pubPort: 6000
    ZMQServiceSubscriber.init("5000", "6000")
    println("Initialized both aspects")

    var msg = 0
    while (msg < 10) {
      println("while loop")
      ZMQServiceServer.push(msg)
      ZMQServiceServer.pub(msg)
      msg = msg+1
      println("while loop: "+msg+" going to sub pull")
      ZMQServiceSubscriber.sub()
      ZMQServiceSubscriber.pull()
      Thread sleep 1000
    }
    ZMQServiceServer.end()
  }
}
