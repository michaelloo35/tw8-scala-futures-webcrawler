package webcrawler

import java.io._
import java.net.URL

import org.htmlcleaner.HtmlCleaner

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebCrawler extends App {
  val url = "http://galaxy.agh.edu.pl/~balis/dydakt/tw/"


  val cleaner = new HtmlCleaner
  val props = cleaner.getProperties

  crawlUntilDepth(2, url)
  readLine()

  def crawlSingleSite(site: String, depth: Int): Future[Iterable[String]] = Future {
    val url = new URL(site)

    val rootNode = cleaner.clean(url)
    val elements = rootNode.getElementsByName("a", true)

    val urls = elements.map(e => e.getAttributeByName("href"))
    urls
  }

  def crawlUntilDepth(maxDepth: Int, root: String, depth: Int = 0): Unit = Future {

//    // creating directory
//    val sb = new StringBuilder
//    sb.append(".")
//    for (i <- 0 to depth) {
//      sb.append("/" + i)
//    }
//    val path = sb.toString()
//    val directory = new File(path)
//    if (!directory.exists) {
//      directory.mkdir
//    }
//
//    // creating site file
//    println(path)
//    val host = root.split(".")
//    val file = new File(path + "/" + host + ".txt")
//    val bw = new BufferedWriter(new FileWriter(file))
//    bw.write(root)
//    bw.close()

    println(root + " on depth: " + depth)
    if (depth >= maxDepth) {}
    else crawlSingleSite(root, depth) onComplete {

      case Success(urls) => urls.foreach(href => crawlUntilDepth(maxDepth, href, depth + 1))

      case Failure(_) =>
    }
  }
}
