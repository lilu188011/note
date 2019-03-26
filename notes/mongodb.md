# mongodb学习 #
- 启动
----------
	mongod --dbpath="c:\data\db" 启动服务端
	mongo 连接
	mongo --port 28015 连接指定端口
	mongo mongodb://mongodb0.example.com:28015
	mongo --host mongodb0.example.com --port 28015
	mongo --username alice --password --authenticationDatabase admin --host mongodb0.examples.com --port 28015

-常用命令
----------
	db  查看所有数据库

	use 使用数据库(如不存在 插入数据创建)

	show dbs 获取当前可用的库

	db.getSiblingDB() 跨库访问

	db.myCollection.insertOne()  创建集合 插入数据 (mycollection 集合名称)

	db.getCollection("3 test").find() | db.myCollection.find().pretty()  获取集合数据
	
	x = 1; count=1;if ( x > 0 ) { count++; print (x); }; |  mongo表达式

	db.myCollection.c  按table键自动补全

	cmdCount = 1;
	prompt = function() {
             return (cmdCount++) + "> ";
    }

	host = db.serverStatus().host;
	prompt = function() {
             return db+"@"+host+"$ ";
    }     mongodb自定义shell提示符


	export EDITOR=vim
	mongo			在mongo启动前设置shell使用编辑器
	
	


- 方法
----------
	方法定义:  function myFunction () { };

	编辑方法:  edit myFunction;
	

	
- 类型
----------
	var myDateString = Date();
	查看类型		typeof myDateString
	var myDate = new Date();
	var myDateInitUsingISODateWrapper = ISODate();	
	instanceof 验证类型


	new ObjectId
	  
	NumberLong("2090845886852")

	$inc  将字段递增
	db.collection.updateOne( { _id: 10 },
                      { $inc: { calc: 5 } } )


	NumberInt() 

	NumberDecimal("1000.55")

	
	db.inventory.find( { price: { $type: "decimal" } } )


- 查询操作
----------
	db.inventory.find( { status: { $in: [ "A", "D" ] } } )   in操作


	db.inventory.find( { status: "A", qty: { $lt: 30 } } ) 小于

	db.inventory.find( { $or: [ { status: "A" }, { qty: { $lt: 30 } } ] } )  or操作

	
	db.inventory.find( {
     status: "A",
     $or: [ { qty: { $lt: 30 } }, { item: /^p/ } ]
	} )  正则表达式


- 嵌套(对象)查询	
----------
	db.inventory.find( { size: { h: 14, w: 21, uom: "cm" } } )  查询字段全匹配(包括字段顺序)


	db.inventory.find( { "size.uom": "in" } ) 查询内联字段

	db.inventory.find( { "size.h": { $lt: 15 } } )

	

- 数组查询
----------
	db.inventory.find( { tags: ["red", "blank"] } ) 精确查询,与顺序有关

	db.inventory.find( { tags: { $all: ["red", "blank"] } } )  $all 忽略顺序

	db.inventory.find( { tags: "red" } )  至少包含red


	db.inventory.find( { dim_cm: { $gt: 25 } } )  包含一个 大于25的元素


	db.inventory.find( { dim_cm: { $gt: 15, $lt: 20 } } )  >15  <20    15<x<20


	db.inventory.find( { dim_cm: { $elemMatch: { $gt: 22, $lt: 30 } } } )  
	指定条件   22<x<30

	db.inventory.find( { "dim_cm.1": { $gt: 25 } } )   指定索引查询

	db.inventory.find( { "tags": { $size: 3 } } ) 根据数组大小查询


- 数组内嵌套对象查询
----------
		{ item: "journal", instock: [ { warehouse: "A", qty: 5 }, 
			{ warehouse: "C", qty: 15 }]}


		db.inventory.find( { "instock": { warehouse: "A", qty: 5 } } ) 精确查询


		db.inventory.find( { 'instock.qty': { $lte: 20 } } )  查询数组中对象 qty < 20


		db.inventory.find( { 'instock.0.qty': { $lte: 20 } } ) 带角标查询


		db.inventory.find( { "instock": { $elemMatch: { qty: 5, warehouse: "A" } } } )  指定条件


		db.inventory.find( { status: "A" }, { item: 1, status: 1 } )  查询返回指定字段


		db.inventory.find( { status: "A" }, { item: 1, status: 1, _id: 0 } ) 将
		查询字段设为0 表示排除该字段  1 表示查询该字段


		db.inventory.find( { status: "A" }, { item: 1, status: 1, instock: { $slice: -1 } } )  返回数组最后一个字段

		
		db.inventory.find( { item: null } )  查询空字段

		db.inventory.find( { item : { $type: 10 } } )  查询时 类型检查

		db.inventory.find( { item : { $exists: false } } )  查询不包含 item字段

- cursor
----------
	遍历方式一
	var myCursor = db.users.find( { type: 2 } );
	while (myCursor.hasNext()) {
	   printjson(myCursor.next());
	}	
	
	遍历方式二
	var myCursor =  db.users.find( { type: 2 } );
	myCursor.forEach(printjson);

	方式三 转为数组 下标取值
	var myCursor = db.inventory.find( { type: 2 } );
	var documentArray = myCursor.toArray();
	var myDocument = documentArray[3];
	

	var myCursor = db.users.find().noCursorTimeout();  无时间限制(默认10分钟)

	objsLeftInBatch 查看游标中还剩多少元素				
	var myCursor = db.inventory.find();
	var myFirstDocument = myCursor.hasNext() ? myCursor.next() : null;
	myCursor.objsLeftInBatch();


	db.serverStatus().metrics.cursor;  查看服务器游标信息


- 更新操作 
----------
	更新单一文档
	db.inventory.updateOne(
	   { item: "paper" },
	   {
	     $set: { "size.uom": "cm", status: "P" },
	     $currentDate: { lastModified: true }
	   }
	)

	批量更新
	db.inventory.updateMany(
	   { "qty": { $lt: 50 } },
	   {
	     $set: { "size.uom": "in", status: "P" },
	     $currentDate: { lastModified: true }
	   }
	)

	替换文档
	db.inventory.replaceOne(
	   { item: "paper" },
	   { item: "paper", instock: [ { warehouse: "A", qty: 60 }, 
	   { warehouse: "B", qty: 40 } ] }
	)

- 删除操作
----------
	db.inventory.deleteMany({})  删除所有文档

	db.inventory.deleteMany({ status : "A" })  删除符合条件文档

	db.inventory.deleteOne( { status: "D" } )

- 批量执行命令
----------
	bulkWrite() supports the following write operations:
		insertOne
		updateOne
		updateMany
		replaceOne
		deleteOne
		deleteMany	

		try {
		   db.characters.bulkWrite(
		      [
		         { insertOne :
		            {
		               "document" :
		               {
		                  "_id" : 4, "char" : "Dithras", "class" : "barbarian", "lvl" : 4
		               }
		            }
		         },
		         { insertOne :
		            {
		               "document" :
		               {
		                  "_id" : 5, "char" : "Taeln", "class" : "fighter", "lvl" : 3
		               }
		            }
		         },
		         { updateOne :
		            {
		               "filter" : { "char" : "Eldon" },
		               "update" : { $set : { "status" : "Critical Injury" } }
		            }
		         },
		         { deleteOne :
		            { "filter" : { "char" : "Brisbane"} }
		         },
		         { replaceOne :
		            {
		               "filter" : { "char" : "Meldane" },
		               "replacement" : { "char" : "Tanys", "class" : "oracle", "lvl" : 4 }
		            }
		         }
		      ]
		   );
		}
		catch (e) {
		   print(e);
		}

- mongo 和 sql 区别 对应关系
----------
	
[https://docs.mongodb.com/manual/reference/sql-comparison/](https://docs.mongodb.com/manual/reference/sql-comparison/)


- 文本查询
----------
	db.stores.createIndex( { name: "text", description: "text" } )
	 对 name,description创建文本索引

	
	使用$text,$search 来索引文档
	db.stores.find( { $text: { $search: "java coffee shop" } } )
	
	使用双引号 精确查询
	db.stores.find( { $text: { $search: "\"coffee shop\"" } } )
	使用- 排除  查找 包含java shop 但不包含 coffee
	db.stores.find( { $text: { $search: "java shop -coffee" } } )
	使用 $meta: "textScore"  显示评分信息
	db.stores.find(
	   { $text: { $search: "java coffee shop" } },
	   { score: { $meta: "textScore" } }
	).sort( { score: { $meta: "textScore" } } )


- 聚合
----------
1. 	分组
	1. 	
	[https://docs.mongodb.com/manual/_images/aggregation-pipeline.bakedsvg.svg](https://docs.mongodb.com/manual/_images/aggregation-pipeline.bakedsvg.svg)


	
2. map-reduce
	1. 
	[https://docs.mongodb.com/manual/_images/map-reduce.bakedsvg.svg](https://docs.mongodb.com/manual/_images/map-reduce.bakedsvg.svg)

3. distinct
	1. 
	[https://docs.mongodb.com/manual/_images/distinct.bakedsvg.svg](https://docs.mongodb.com/manual/_images/distinct.bakedsvg.svg)	

4. pipeline 
	1.   
	[https://docs.mongodb.com/manual/_images/aggregation-pipeline.bakedsvg.svg](https://docs.mongodb.com/manual/_images/aggregation-pipeline.bakedsvg.svg)




	