<a href="#" class="dropdown-toggle" data-toggle="dropdown">
    <i class="fa fa-user"></i>
    <span>Profile/Log Out <i class="caret"></i></span>
</a>

<ul class="dropdown-menu">
                
    <!-- User image -->
    <li class="user-header bg-light-blue">
        <img src="/_static/img/elephant.png" class="img-circle" alt="User Image" />
        <p>
            ${user.name}
            <small>${user.memberSinceString}</small>
        </p>
    </li>
    
    <!-- Menu Footer-->
    <li class="user-footer">
        <div class="pull-left">
            <a href="${user.profileUrl}" class="btn btn-default btn-flat">Profile</a>
        </div>
        <div class="pull-right">
            <a href="${logoutUrl}" class="btn btn-default btn-flat">Log Out</a>
        </div>
    </li>
     
</ul>