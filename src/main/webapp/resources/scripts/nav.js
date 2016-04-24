			//page导航逻辑
			function PageNav(page, maxPage, navId) {
				this.toPage=function(page){
					console.log('you want to go page '+page+',but you must be override to function toPage,please call setToPage set the callback');
				};
				var step = 2;
				var scope = [];
				var self=this;
				// 总共可视范围大于总数，全部显示
				if (page - step <= 0 && (page + step) >= maxPage) {
					for ( var i = 0; i < maxPage; i++) {
						scope.push(i + 1);
					}
				}
				// 前面不够，后面补
				else if (page - step <= 0) {
					for ( var i = 0; i < (2*step + 1)&&i<maxPage; i++) {
						scope.push(i + 1);
					}
				}
				// 后面不够，前面补
				else if (page + step >= maxPage) {
					for ( var i = maxPage-2*step-1; i < maxPage; i++) {
						if (i >= 0) {
							scope.push(i+1);
						}
					}
				}
				// 都够
				else {
					for ( var i = page - step; i < page + step + 1; i++) {
						scope.push(i);
					}
				}
				var insertHtml = "";
				$(scope).each(
						function(i, n) {
							insertHtml += "<a class='navAPage " + ((n == page) ? 'active' : '')+"' id='page_"+n+"'"
									+ " data-page='" + n + "'>" + n
									+ "</a>&nbsp;&nbsp;";
						});
				$('#'+navId).html(insertHtml);
				/**
				 * 请务必覆盖次方法，
				 * 例如
				 * cos.pageNav.setToPage(function(page){
				 * 			//bla...bla...bla
				 * });
				 * */
				this.setToPage=function(callback){
					self.toPage=callback;
				};
				//绑定首页点击时间
				$('.first').on('click',function(){
					self.toPage(1);
				});
				//绑定尾页点击时间
				$('.last').on('click',function(){
					self.toPage(maxPage);
				});
				
				//绑定下一页触发事件
				$('.next').on('click',function(){
					self.toPage(page+1);
				});
				
				//绑定上一页触发事件
				$('.prev').on('click',function(){
					self.toPage(page-1);
				});
				
				//绑定导航页数事件
				$('.navAPage').live('click',function(){  
					self.toPage($(this).data("page"));
				});
			}