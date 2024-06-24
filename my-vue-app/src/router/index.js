import { createRouter, createWebHistory } from 'vue-router';
import ExampleComponent from '../components/ExampleComponent.vue';
import ConstrainedFrame from '../components/ConstrainedFrame.vue';
import GetPosts from '../components/GetPosts.vue'; // 변경된 컴포넌트 이름
import BoardComponent from '../components/BoardComponent.vue'; // 새 컴포넌트 추가

const routes = [
  {
    path: '/ExampleComponent',
    name: 'ExampleComponent',
    component: ExampleComponent,
  },
  {
    path: '/ConstrainedFrame',
    name: 'ConstrainedFrame',
    component: ConstrainedFrame,
    children: [
          {
            path: 'posts',
            name: 'GetPosts',
            component: GetPosts,
          },
          {
            path: 'board',
            name: 'BoardComponent',
            component: BoardComponent, // 새 경로 추가
          },
        ],
  },
  /*{
    path: '/posts',
    name: 'GetPosts',
    component: GetPosts,
  },
  {
    path: '/board',
    name: 'BoardComponent',
    component: BoardComponent, // 새 경로 추가
  },*/
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
