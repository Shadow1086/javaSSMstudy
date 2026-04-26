import {createRouter, createWebHistory} from "vue-router";
import List from "../components/List.vue";
import AddItem from "../components/AddItem.vue";

const router = createRouter({
		history: createWebHistory(),
		routes: [
			{
				path: "/",
				component: List
			},
			{
				path:"/add",
				component: AddItem
			}
		]
	})
export default router;