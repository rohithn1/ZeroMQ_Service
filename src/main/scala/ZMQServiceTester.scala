package scala

object ZMQServiceTester {
  def main (args: Array[String]): Unit = {
    ZMQServiceServer.init("5000", "6000") //pushPort: 5000, //pubPort: 6000
    ZMQServiceSubscriber.init("5000", "6000")

    var msg = 0
    while (msg < 10) {
      ZMQServiceServer.push(msg)
      ZMQServiceServer.pub(msg)
      println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
      ZMQServiceSubscriber.pull()
      ZMQServiceSubscriber.sub()
      println("--------------------------------------------------")
      println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
      println("--------------------------------------------------")
      msg = msg+1
      Thread sleep 1000
    }
    ZMQServiceServer.end()
    ZMQServiceSubscriber.end()
  }
}
