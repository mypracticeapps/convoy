<template>
  <div class="repo-container bg-2">
    <div class="repo-nav bg-3">
      <div class="repo-title">
        <h5 class="text-left">Convoy App</h5>
        <h6 class="text-left">srikrishna-s/convoy</h6>
      </div>
      <div class="repo-title">
        <h5 class="text-left">Todo App</h5>
        <h6 class="text-left">srikrishna-s/todo</h6>
      </div>
      <div class="repo-title">
        <h5 class="text-left">Boot Starter</h5>
        <h6 class="text-left">srikrishna-s/boot-starter</h6>
      </div>
      <div class="repo-title">
        <h5 class="text-left">Build</h5>
        <h6 class="text-left">srikrishna-s/build</h6>
      </div>
    </div>
    <div class="repo-body bg-4"></div>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";

export default {
  name: "home",
  components: {
    HelloWorld
  }
};
</script>
