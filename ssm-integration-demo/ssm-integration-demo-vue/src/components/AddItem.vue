<script setup lang="ts">
import router from "../router/router.ts";
import instance from "../axios/axios.ts";

const scheduleItem = {
    title: "",
    completed: 0,
}


function goback() {
    router.push("/");
}

async function submit() {
    let {data} = await instance.post("/schedule",scheduleItem);
    if (data.code !== 200) {
        alert("出错了")
        return;
    }
    alert("添加成功，请返回首页查看");
}
</script>

<template>
    <div class="container">
        <div class="welcome">添加日程</div>
        <div class="form">
            <label for="title">日程：</label><input type="text" placeholder="请输入您的日程" id="title"
                                                   v-model="scheduleItem.title">
            <label for="completed">是否完成</label>
            <input type="radio" name="radioCompleted" v-model="scheduleItem.completed" :value="1">已完成
            <input type="radio" name="radioCompleted" v-model="scheduleItem.completed" :value="0">未完成
        </div>
        <div class="btnlist">
            <button @click="submit()">提交</button>
            <button @click="goback()">返回首页</button>
        </div>
    </div>
</template>

<style scoped>

</style>