package org.tlh.warehouse.table.dws

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.tlh.warehouse.util.AppConfig

/**
  * @author 离歌笑
  * @desc
  * @date 2022-01-28
  */
object GoodsItemTopic extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.enableCheckpointing(3 * 1000)
  env.setParallelism(3)
  val tableEnv = StreamTableEnvironment.create(env)

  // 定义商品纬度表
  tableEnv.executeSql(
    s"""
       |create table dwd_dim_goods_info(
       |	  `sku_id` int COMMENT '商品货品表的货品ID',
       |  	`goods_sn` string COMMENT '商品编号',
       |  	`spu_id` int COMMENT '商品表的ID',
       |  	`name` string COMMENT '商品名称',
       |  	`category_id` int COMMENT '商品所属一级类目ID',
       |  	`category_name` string comment '商品所属一级类目名称',
       |  	`category2_id` int COMMENT '商品所属二级类目ID',
       |  	`category2_name` string comment '商品所属二级类目名称',
       |  	`brand_id` int comment '品牌ID',
       |  	`brand_name` string comment '品牌名称',
       |  	`brief` string COMMENT '商品简介',
       |  	`unit` string COMMENT '商品单位，例如件、盒',
       |  	`product_price` decimal(10,2) comment '商品货品表的价格',
       |  	`counter_price` decimal(10,2) COMMENT '专柜价格',
       |  	`retail_price` decimal(10,2) COMMENT '零售价格',
       |  	`add_time` string COMMENT '创建时间',
       |    PRIMARY KEY (sku_id) NOT ENFORCED
       |)comment '商品维度表'
       |WITH (
       |  'connector' = 'upsert-kafka',
       |  'topic' = '${AppConfig.KAFKA_OUTPUT_DWD_DB_GOODS}',
       |  'properties.bootstrap.servers' = '${AppConfig.KAFKA_SERVERS}',
       |  'properties.group.id' = 'dwd_db_goods',
       |  'key.format' = 'json',
       |  'value.format' = 'json'
       |)
    """.stripMargin)

  // 定义订单详情表
  tableEnv.executeSql(
    s"""
       |create table dwd_fact_order_goods_info(
       |	`id` int,
       |	`order_id` int COMMENT '订单表的订单ID',
       |	`goods_id` int COMMENT '商品表的商品ID  spu_id',
       |	`goods_name` string COMMENT '商品名称',
       |	`goods_sn` string COMMENT '商品编号',
       |	`product_id` int COMMENT '商品货品表的货品ID sku_id',
       |	`number` smallint COMMENT '商品货品的购买数量',
       |	`price` decimal(10,2)  COMMENT '商品货品的售价',
       |	`add_time` string COMMENT '创建时间',
       |  order_time AS to_timestamp_ltz(cast(add_time as bigint),3),
       |	`user_id` int comment '用户id',
       | `province` int COMMENT '省份ID',
       | `city` int COMMENT '城市ID',
       | `country` int COMMENT '乡镇ID',
       | PRIMARY KEY(id) NOT ENFORCED,
       | WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND
       |)comment '订单详情事实表'
       |WITH (
       |  'connector' = 'upsert-kafka',
       |  'topic' = '${AppConfig.KAFKA_OUTPUT_DWD_DB_ORDER_GOODS}',
       |  'properties.bootstrap.servers' = '${AppConfig.KAFKA_SERVERS}',
       |  'properties.group.id' = 'dws_db_order_goods',
       |  'key.format' = 'json',
       |  'value.format' = 'json'
       |)
    """.stripMargin)

  // 创建视图进行关联
  tableEnv.executeSql(
    """
      |create view order_goods_wide_view
      |as
      |(
      |	select
      |		og.id,
      |		og.order_id,
      |		og.goods_id as spu_id,
      |		o.sku_id,
      |		o.goods_sn,
      |		og.goods_name,
      |		og.number,
      |		og.price,
      |		o.category_id,
      |		o.category_name,
      |		o.category2_id,
      |		o.category2_name,
      |		o.brand_id,
      |		o.brand_name,
      |		o.product_price,
      |		o.counter_price,
      |		o.retail_price
      |	from dwd_fact_order_goods_info og
      |	left join dwd_dim_goods_info o
      |	on og.goods_id=o.spu_id
      |)
    """.stripMargin)

  tableEnv.executeSql(
    """
      |select * from order_goods_wide_view
    """.stripMargin)
    .print()


}
