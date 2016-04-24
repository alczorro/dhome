/**全选控件
 * @param allId 全选的id
 * @param for出来的item的class
 * */
function checkAllBox(allId,itemClass){
	var $all=$('#'+allId);
	$all.die('click').live('click',function(){
		var checked=$all.attr("checked");
		$('.'+itemClass).each(function(i,n){
			if(checked){
				$(n).attr("checked","checked");
			}else{
				$(n).removeAttr("checked");
			}
		});
	});
	$('.'+itemClass).die('click').live('click',function(){
		var allSelect=true;
		$('.'+itemClass).each(function(i,n){
			if($(n).attr("checked")){
				allSelect&=true;
			}else{
				allSelect&=false;	
			}
		});
		if(allSelect){
			$all.attr("checked","checked");
		}else{
			$all.removeAttr("checked");
		}
	});
};