<template>
  <div id="app" class="root-container">
    <div class="nav-container">
      <nav class="navbar navbar-expand-md navbar-dark bg-dark sidebarNavigation"
           data-sidebarClass="navbar-dark bg-dark">
        <div class="container-fluid">
          <a class="navbar-brand" href="#">Gatekeeper</a>
        </div>
      </nav>
    </div>
    <div class="root-body">
      <router-view></router-view>
    </div>
  </div>
</template>

<style lang="scss">
  #app {
    /*font-family: "Avenir", Helvetica, Arial, sans-serif;*/
    /*-webkit-font-smoothing: antialiased;*/
    /*-moz-osx-font-smoothing: grayscale;*/
    /*color: #2c3e50;*/
  }
</style>
