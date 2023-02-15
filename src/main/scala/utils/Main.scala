package utils

import org.apache.spark.graphx.Graph
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes.GraphFrame

import java.io.PrintWriter

import graph.JavaDriver


object Main extends App {

  val conf: SparkConf = new SparkConf().setAppName("SparkTest").setMaster("local[*]").set("spark.executor.memory", "16g")
  val sparkContext = new SparkContext(conf)
  val sparkSession: SparkSession = SparkSession.builder.config(sparkContext.getConf).getOrCreate()
  val sqlContext: SQLContext = sparkSession.sqlContext


  if (true) {
    val v = sqlContext.createDataFrame(List(
      ("a", "Alice", 34),
      ("b", "Bob", 36),
    )).toDF("id", "name", "age")

    val e = sqlContext.createDataFrame(List(
      ("a", "b", "friend"),
    )).toDF("src", "dst", "relationship")

    val g = GraphFrame(v, e)
    val subgraphX = g.toGraphX
    val pw = new PrintWriter("src\\main\\resources\\gexf\\" + "friends" + ".gexf")
    val gexfString = toGexf(subgraphX)
    pw.write(gexfString)
    pw.close()
  }


  if (true) {
    // import java classes
    class ScalaDriver extends JavaDriver {
      runGEXFtoSVG("friends", "friends")

      runSVGtoPNG("src\\main\\resources\\svg\\friends.svg",
        "src\\main\\resources\\png\\friends.png")
    }

    // run imported java classes
    new ScalaDriver
  }


  def toGexf[VD, ED](g: Graph[VD, ED]): String = {
    val header =
      """<?xml version="1.0" encoding="UTF-8"?>
        |<gexf xmlns="https://www.gexf.net/1.2draft" version="1.2">
        |<meta>
        |<description>A gephi graph in GEXF format</description>
        |</meta>
        |<graph mode="static" defaultedgetype="directed">
        |<attributes class="node">
        |<attribute id="1" title="redirect" type="string"/>
        |<attribute id="2" title="namespace" type="string"/>
        |<attribute id="3" title="category" type="string"/>
        |</attributes>
      """.stripMargin


    val vertices = "<nodes>\n" + g.vertices.map(
      v => s"""<node id=\"${v._1}\" label=\"${v._2.asInstanceOf[Row].getAs("id")}\">\n
      </node>"""
    ).collect.mkString + "</nodes>\n"

    val edges = "<edges>\n" + g.edges.map(
      e =>
        s"""<edge source=\"${e.srcId}\" target=\"${e.dstId}\"
  label=\"${e.attr}\"/>\n"""
    ).collect.mkString + "</edges>\n"

    val footer = "</graph>\n</gexf>"

    header + vertices + edges + footer
  }

}
