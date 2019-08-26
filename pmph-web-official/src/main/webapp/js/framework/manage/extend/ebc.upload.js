//提供外部调用的方法集
var ebcUpload = {
	//图片附件处理
//	{
//	    "id":-1,
//	    "fileno":"3f7f02d1-1f6b-41e2-aadb-d54f7b5ed1d2",
//	    "type":"2",
//	    "file_name":"b1dd551de4a54f1fbbfc0547134f9b5a.zip",
//	    "file_size":38268,
//	    "folderid":"0",
//	    "file_width":0,
//	    "file_height":0,
//	    "old_file_name":"GWTDesigner_v7.2.0_for_Eclipse3.3.zip",
//	    "orgnode_id":67,
//	    "ext_name":".zip",
//	    "filename":"b1dd551de4a54f1fbbfc0547134f9b5a.zip",
//	    "path":"uploads\\201206\\",
//	    "small_path":"uploads\\201206\\",
//	    "small_file_name":"b1dd551de4a54f1fbbfc0547134f9b5a.zip",
//	    "userid":1,
//	    "simpleImgURL":"uploads/201206/b1dd551de4a54f1fbbfc0547134f9b5a.zip",
//	    "originalImgURL":"uploads/201206/b1dd551de4a54f1fbbfc0547134f9b5a.zip"
//	}
	attachHandle : function(objs){		
		var imghtml = '';
		$.each(objs,function(i,d){
			if(d && d.type){
				if(d.type=='1'){//图片类型
					imghtml += '<img ';
					if(d.originalImgURL)imghtml += ' src="'+d.originalImgURL+'"';
					if(d.alt && d.alt!=-1)imghtml += ' alt="'+d.alt+'"';
					if(d.align && d.align!=-1)imghtml += ' align="'+d.align+'"';
					if(d.file_width && d.file_width!=-1)imghtml += ' width="'+d.file_width+'"';
					if(d.file_height && d.file_height!=-1)imghtml += ' height="'+d.file_height+'"';
					if(d.border && d.border!=-1)imghtml += ' border="'+d.border+'"';
					if(d.vspace && d.vspace!=-1)imghtml += ' vspace="'+d.vspace+'"';
					if(d.hspace && d.hspace!=-1)imghtml += ' hspace="'+d.hspace+'"';
					imghtml += ' /><br/>\n';
				}else if(d.type=='2'){//其它附件类型
					imghtml += '<a ';
					if(d.originalImgURL)imghtml += ' href="'+d.originalImgURL+'" target="_blank" ';
					imghtml += ' >' + d.filename + '</a><br/>\n';
				}
			}
		});
		return imghtml;
	},
	//其它附件处理
	otherHandle : function(objs){
		
	}
};