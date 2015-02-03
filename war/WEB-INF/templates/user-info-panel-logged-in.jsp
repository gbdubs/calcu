<div class="pull-left image">
    <img src="${profilePictureUrl}" class="img-square" alt="User Image" />
</div>

<div class="pull-left info">
    <p>${username}</p>
    <div class="progress progress-striped">
        <div class="progress-bar progress-bar-primary" role="progressbar" style="width:${karmaNumber * 100/(nextLevelKarmaThreshold - lastLevelKarmaThreshold)}%">
        
        </div>
        <span class="level-display">Level ${userLevel}</span>
    </div>
</div>