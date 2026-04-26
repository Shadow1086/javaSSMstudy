<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import instance from "../axios/axios.ts";

interface Schedule {
    id: number;
    title: string;
    completed: number | boolean;
}

interface Result<T> {
    code: number;
    message: string;
    data: T;
}

interface PageInfoVo<T> {
    pageSize: number;
    currentPage: number;
    total: number;
    data: T[];
}

const router = useRouter();
const schedules = ref<Schedule[]>([]);
const pageSize = ref(5);
const currentPage = ref(1);
const total = ref(0);
const loading = ref(false);
const errorMessage = ref("");
const editingId = ref<number | null>(null);
const editForm = ref<Schedule>({
    id: 0,
    title: "",
    completed: 0
});

const loadSchedules = async () => {
    loading.value = true;
    errorMessage.value = "";

    try {
        const params = new URLSearchParams();
        params.append("pageSize", String(pageSize.value));
        params.append("currentPage", String(currentPage.value));

        const response = await instance.get("/schedule", {params: params});

        const result = response.data as Result<PageInfoVo<Schedule>>

        schedules.value = result.data?.data || [];
        total.value = result.data?.total || 0;
    } catch (error) {
        errorMessage.value = "网络异常，暂时无法查询日程列表";
    } finally {
        loading.value = false;
    }
};

const formatCompleted = (completed: number | boolean) => {
    return completed === true || completed === 1 ? "已完成" : "未完成";
};

const toAddPage = () => {
    router.push("/add");
};

async function deleteSchedule(id: number) {
    if (!window.confirm("确定要删除这条日程吗？")) {
        return;
    }

    try {
        const {data} = await instance.delete(`/schedule/${id}`);
        if (data.code !== 200) {
            alert("删除失败");
            return;
        }

        await loadSchedules();
    } catch (error) {
        alert("删除失败");
    }
}

function startUpdate(schedule: Schedule) {
    editingId.value = schedule.id;
    editForm.value = {
        id: schedule.id,
        title: schedule.title,
        completed: schedule.completed === true || schedule.completed === 1 ? 1 : 0
    };
}

function cancelUpdate() {
    editingId.value = null;
}

async function submitUpdate() {
    if (editForm.value.title.trim() === "") {
        alert("日程标题不能为空");
        return;
    }

    try {
        const {id, title, completed} = editForm.value;
        const {data} = await instance.put(`/schedule/${id}`, {
            title,
            completed
        });
        if (data.code !== 200) {
            alert("修改失败");
            return;
        }

        editingId.value = null;
        await loadSchedules();
    } catch (error) {
        alert("修改失败");
    }
}

onMounted(() => {
    loadSchedules();
});
</script>

<template>
    <div class="container">
        <div class="toolbar">
            <h2>日程列表</h2>
            <button type="button" @click="toAddPage">添加</button>
        </div>

        <div v-if="loading" class="status">正在加载...</div>
        <div v-else-if="errorMessage" class="status error">{{ errorMessage }}</div>
        <div v-else-if="schedules.length === 0" class="status">暂无日程数据</div>

        <div v-else class="list">
            <table>
                <thead>
                <tr>
                    <th>编号</th>
                    <th>日程标题</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="schedule in schedules" :key="schedule.id">
                    <td>{{ schedule.id }}</td>
                    <td v-if="editingId === schedule.id">
                        <input class="title-input" v-model="editForm.title" type="text">
                    </td>
                    <td v-else>{{ schedule.title }}</td>

                    <td v-if="editingId === schedule.id">
                        <div class="radio-group">
                            <label>
                                <input type="radio" v-model="editForm.completed" :value="1">
                                已完成
                            </label>
                            <label>
                                <input type="radio" v-model="editForm.completed" :value="0">
                                未完成
                            </label>
                        </div>
                    </td>
                    <td v-else>{{ formatCompleted(schedule.completed) }}</td>

                    <td class="btn-list">
                        <template v-if="editingId === schedule.id">
                            <button @click="submitUpdate">保存</button>
                            <button class="secondary-btn" @click="cancelUpdate">取消</button>
                        </template>
                        <template v-else>
                            <button class="danger-btn" @click="deleteSchedule(schedule.id)">删除</button>
                            <button @click="startUpdate(schedule)">修改</button>
                        </template>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="pager">
            <span>当前第 {{ currentPage }} 页</span>
            <span>每页 {{ pageSize }} 条</span>
            <span>共 {{ total }} 条</span>
        </div>
    </div>
</template>

<style scoped>
.container {
    width: 800px;
    margin: 40px auto;
}

.toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
}

.toolbar h2 {
    margin: 0;
    font-size: 22px;
    font-weight: 600;
}

button {
    padding: 8px 16px;
    border: 1px solid #1677ff;
    border-radius: 4px;
    color: #ffffff;
    background-color: #1677ff;
    cursor: pointer;
}

button:hover {
    background-color: #0958d9;
}

.secondary-btn {
    border-color: #d9d9d9;
    color: #333333;
    background-color: #ffffff;
}

.secondary-btn:hover {
    background-color: #f5f5f5;
}

.danger-btn {
    border-color: #ff4d4f;
    background-color: #ff4d4f;
}

.danger-btn:hover {
    background-color: #d9363e;
}

.list {
    border: 1px solid #dddddd;
    border-radius: 4px;
    overflow: hidden;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    padding: 12px 16px;
    border-bottom: 1px solid #eeeeee;
    text-align: left;
}

th {
    background-color: #f5f5f5;
    font-weight: 600;
}

tbody tr:last-child td {
    border-bottom: none;
}

tbody tr:hover {
    background-color: #fafafa;
}

.btn-list {
    display: flex;
    gap: 8px;
}

.title-input {
    width: 100%;
    box-sizing: border-box;
    padding: 7px 10px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    font-size: 14px;
}

.title-input:focus {
    border-color: #1677ff;
    outline: none;
}

.radio-group {
    display: flex;
    gap: 12px;
}

.radio-group label {
    display: flex;
    align-items: center;
    gap: 4px;
    white-space: nowrap;
}

.status {
    padding: 24px;
    border: 1px solid #eeeeee;
    border-radius: 4px;
    color: #666666;
    text-align: center;
}

.error {
    color: #cf1322;
    background-color: #fff1f0;
    border-color: #ffa39e;
}

.pager {
    display: flex;
    gap: 16px;
    justify-content: flex-end;
    margin-top: 12px;
    color: #666666;
    font-size: 14px;
}
</style>
