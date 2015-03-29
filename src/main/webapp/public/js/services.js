var app=angular.module("services", [])


app.service("dateService",function(){
	this.sameDate=function(d1,d2){
		return d1.getFullYear()==d2.getFullYear()
		&&d1.getDate()==d2.getDate()
		&&d1.getMonth()==d2.getMonth();
	};
	this.setSameDate=function(d1,d2){
		d1.setFullYear(d2.getFullYear());
		d1.setMonth(d2.getMonth());
		d1.setDate(d2.getDate());
		return new Date(d1.getTime());
	};
	this.stringToDate=function(act){
		act.endTime = act.endTime!=null ? new Date(
			act.endTime)
			: null;
		act.startTime = act.startTime!=null ? new Date(
			act.startTime)
			: null;
	};
	this.emptyTime=function(date){
		date=new Date(date);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setMilliseconds(0);
		return [new Date(date),new Date(date.setDate(date.getDate()+1))];
	}
	this.emptyDate=function(date){
		date=new Date(date);
		date=this.emptyTime(date);
		date.setDate(0);
		return [new Date(date),new Date(date.setMonth(date.getMonth()+1))];
	}
	this.emptyDay=function(date){
		date=new Date(date);
		date=this.emptyTime(date);
		date.setDate(date.getDate()-date.getDay());
		return [new Date(date),new Date(date.setDate(date.getDate()+7))];
	}
	
})


app.service("activityService",[ '$http','dateService',function($http,dateService){
	var service=this;
	this.moveToTrash=function(context,aid){
		console.log("move " + aid + " to trash");
		if(aid!=null){
		$http.post("/api/activity/" + aid, {
			parent : 1
		}).success(function() {
			context.buildList();
		})}else{
			context.activities[context.activities.length-2].loaded=false;
			context.buildList();
		}
	}
	this.submit=function(context,callback,parentAid){
		if (context.loaded) {
			context.loaded = false;

			if (context.data.aid == null) {
				context.data.parent = parentAid;
				console.log("new: "
					+ JSON.stringify(context.data));
				$http.post("/api/activity", context.data)
					.success(function() {
						callback();
					});
			} else {
				console.log("edit: "
					+ JSON.stringify(context.data));
				$http.post(
					"/api/activity/" + context.data.aid,
					context.data).success(function() {
						console.log("submit success");
					callback();
				});
			}
		}
	}
	
	//context: activity
	this.loadActivity=function(context,callback){
		console.log(JSON.stringify(context));
		var aidString;
		// new item
		if (typeof(context.data.aid)==="undefined"||context.data.aid==null) {
			console.log("load new");
			aidString = "new";
		} else {
			console.log("load exist:" + context.data.aid);
			aidString = context.data.aid;
		}
		$http
			.get("/api/activity/detail/" + aidString)
			.success(
				function(response) {
					// detail=true means it has been loaded, must be
					// submited
					console.log("load success");
					context.data.note = response.note;
					context.data.name = response.name;
					console.log(JSON.stringify(context.data));
					context.loaded = true;
					if(aidString=="new"){
						dateService.stringToDate(response);
						context.data=response;
					}
					if(typeof(callback)!="undefined"){
						callback(context);
					}
				});
	}
	this.pushNew=function(alist){
		console.log("pushNew alist");
		alist.push({data:{
			name : "New"
		},loaded:false,open:false});
	}
	//context: actListCtrl
	this.buildList=function(context,req,rootAid,withoutNew){
		req.method="GET";
		$http(req)
			.success(
				function(response) {
					// store active activity
					var actStore=[];
					for(iter in context.activities){
						if(context.activities[iter].loaded==true){
							actStore.push(context.activities[iter]);
						}
					}
					console.log("activity stored "+actStore.length);
					context.activities=[];
					for(iter in response){
						context.activities.push({data:response[iter],loaded:false,open:false});
					}
					if(typeof(withoutNew)=="undefined"){
						service.pushNew(context.activities);
					}
					// read ISO string
					for(iter in context.activities){
						dateService.stringToDate(context.activities[iter].data);
					}
					$http.get(
						"/api/activity/path/" + rootAid)
						.success(function(response) {
							console.log(JSON.stringify(response));
							context.actPath = response;
						});
					// recover active activity
					//loaded item exist
					var exist = false;
					for (iter in actStore) {
						console.log("recover:" + actStore[iter].data.aid);
						for (i = 0; i < context.activities.length; i++) {
							if (context.activities[i].data.aid == actStore[iter].data.aid) {
								context.activeActNum = i;
								context.activities[i] = actStore[iter];
								// recover new
								if (i == context.activities.length - 1) {
									service.pushNew(context.activities);
								}
								exist = true;
								break;
							}
						}
					}
					if (!exist) {
						context.activeActNum = -1;
					}
					console.log("build success");
				});
	}
}])