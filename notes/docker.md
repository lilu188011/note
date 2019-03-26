# docker学习 #
1. centos安装docker
----------
	Docker CE 支持 64 位版本 CentOS 7，并且要求内核版本不低于 3.10。 CentOS 7
	满足最低内核的要求，但由于内核版本比较低，部分功能（如  overlay2  存储层
	驱动）无法使用，并且部分功能可能不太稳定。

	
1. 安装docker
	- 卸载旧版本
	----------
		sudo yum remove docker \
		docker-client \
		docker-client-latest \
		docker-common \
		docker-latest \
		docker-latest-logrotate \
		docker-logrotate \
		docker-selinux \
		docker-engine-selinux \
		docker-engine


 
	
	- 添加yum源
	----------
		sudo yum-config-manager \
		--add-repo \
		https://mirrors.ustc.edu.cn/docker-ce/linux/centos/docker-ce
		.repo




	
	- yum安装
	----------
		sudo yum install -y yum-utils \
		device-mapper-persistent-data \
		lvm2

 		测试版
		sudo yum-config-manager --enable docker-ce-test
		每日构建版
		sudo yum-config-manager --enable docker-ce-nightly
	
	

	- 更新yum源 安装docker-ce
	----------
		sudo yum makecache fast
		sudo yum install docker-ce

	
	- 使用脚本自动安装
	----------
		curl -fsSL get.docker.com -o get-docker.sh
		sudo sh get-docker.sh --mirror Aliyun

	
	- 启动 Docker CE
	----------
		sudo systemctl enable docker
		sudo systemctl start docker

	- 建立 docker 用户组
	----------
		sudo groupadd docker
		将当前用户加入  docker  组：
 		sudo usermod -aG docker $USER

	
	- 测试 Docker 是否安装正确
	----------
		docker run hello-world
		Unable to find image 'hello-world:latest' locally
		latest: Pulling from library/hello-world
		d1725b59e92d: Pull complete
		Digest: sha256:0add3ace90ecb4adbf7777e9aacf18357296e799f81cabc9f
		de470971e499788
		Status: Downloaded newer image for hello-world:latest
	
	
	- 镜像加速器
	----------
		对于使用 systemd 的系统，请在  /etc/docker/daemon.json  中写入如下内容
		（如果文件不存在请新建该文件）

		{
			"registry-mirrors": [
			"https://registry.docker-cn.com"
			]
		}
		重启docker
		sudo systemctl daemon-reload
		sudo systemctl restart docker

2.获取镜像
----------
	docker pull ubuntu:18.04

3.运行
----------
	docker run -it --rm \
	ubuntu:18.04 \
	bash

	docker run  就是运行容器的命令，具体格式我们会在 容器 一节进行详细讲解，
	我们这里简要的说明一下上面用到的参数。
	-it  ：这是两个参数，一个是  
	-i  ：交互式操作，一个是 
	-t  终端。我们这里打算进入bash 执行一些命令并查看返回结果，因此我们需要交互式终
		端。
	--rm ：这个参数是说容器退出后随之将其删除。默认情况下，为了排障需
		求，退出的容器并不会立即删除，除非手动  docker rm  。我们这里只是随便
		执行个命令，看看结果，不需要排障和保留结果，因此使用  --rm  可以避免
		浪费空间。
	ubuntu:18.04  ：这是指用  ubuntu:18.04  镜像为基础来启动容器。
	bash  ：放在镜像名后的是命令，这里我们希望有个交互式 Shell，因此用的
	是  bash  。

4.列出镜像
----------
	docker image ls

5.便捷的查看镜像、容器、数据卷所占用的空间
----------
	docker system df

6.虚悬镜像
----------
	上面的镜像列表中，还可以看到一个特殊的镜像，这个镜像既没有仓库名，也没有
	标签，均为  
	<none>  。：
	<none> <none> 00285df0df87 5 d
	ays ago 342 MB
	这个镜像原本是有镜像名和标签的，原来为  mongo:3.2  ，随着官方镜像维护，发
	布了新版本后，重新  docker pull mongo:3.2  时， mongo:3.2  这个镜像名被
	转移到了新下载的镜像身上，而旧的镜像上的这个名称则被取消，从而成为了
	<none>  。除了  docker pull  可能导致这种情况， docker build  也同样可
	以导致这种现象。由于新旧镜像同名，旧镜像名称被取消，从而出现仓库名、标签
	均为  <none>  的镜像。这类无标签镜像也被称为 虚悬镜像(dangling image) ，可
	以用下面的命令专门显示这类镜像：
	列出镜像
	73
	$ docker image ls -f dangling=true
	REPOSITORY TAG IMAGE ID CREA
	TED SIZE
	<none> <none> 00285df0df87 5 da
	ys ago 342 MB
	一般来说，虚悬镜像已经失去了存在的价值，是可以随意删除的，可以用下面的命
	令删除。
	$ docker image prune

7.列出中间层镜像
----------
	docker image ls -a

8.列出部分镜像
----------
	docker image ls ubuntu

	docker image ls --format "{{.ID}}: {{.Repository}}"
	
	docker image ls -f since=mongo:3.2

	docker image ls --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"

9.删除本地镜像
----------
	docker image rm 501

	我们需要删除所有仓库名为  redis  的镜像：
	$ docker image rm $(docker image ls -q redis)

	或者删除所有在  mongo:3.2  之前的镜像：
	$ docker image rm $(docker image ls -q -f before=mongo:3.2)



 	