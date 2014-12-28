<a href="#" class="dropdown-toggle hidden-xs" data-toggle="dropdown">
    <i class="fa fa-user"></i>
    <span>Profile/Log Out <i class="caret"></i></span>
</a>
<a href="#" class="dropdown-toggle visible-xs" data-toggle="dropdown">
    <i class="fa fa-user"></i>
    <span>Log Out <i class="caret"></i></span>
</a>

<ul class="dropdown-menu">
                
    <!-- User image -->
    <li class="user-header bg-light-blue">
        <img src="${profilePictureUrl}" class="img-circle" alt="User Image" />
        <p>
            ${username}
            <small>${email}, ${karma}</small>
        </p>
    </li>
    
    <!-- Menu Footer-->
    <li class="user-footer">
        <div class="pull-left">
            <a href="${profileUrl}" class="btn btn-default btn-flat">Profile</a>
        </div>
        <div class="pull-right">
            <a href="${logoutUrl}" class="btn btn-default btn-flat">Log Out</a>
        </div>
    </li>
     
</ul>