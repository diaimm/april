
<div class="${wrapperClass}">
	<ul class="${ulClass}">
		<#if useFirst && firstIndex??>
			<li><a href="#" onclick="${onClickCallback}('${pagingId}', ${firstIndex}));">«</a></li>
		</#if>

		<#if usePrev && prevIndex??>
			<li><a href="#" onclick="${onClickCallback}('${pagingId}', ${prevIndex});">«</a></li>
		</#if>

		<#list screenFirstIndex .. screenLastIndex as index>
			<#assign liClass=""/>
			<#if index == current>
				<#assign liClass="active"/>
			</#if>
			<li class="${liClass}">
				<a href="#" onclick="${onClickCallback}('${pagingId}', ${index});">${index}</a>
			</li>
		</#list>

		<#if useNext && nextIndex??>
			<li><a href="#" onclick="${onClickCallback}('${pagingId}', ${nextIndex});">»</a></li>
		</#if>

		<#if useLast && lastIndex??>
				<li><a href="#" onclick="${onClickCallback}('${pagingId}', ${lastIndex});">»</a></li>
		</#if>
	</ul>
</div>