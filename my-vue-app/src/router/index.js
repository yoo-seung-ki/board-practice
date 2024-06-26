import { createRouter, createWebHistory } from 'vue-router';
import ExampleComponent from '../components/ExampleComponent.vue';
import ConstrainedFrame from '../components/ConstrainedFrame.vue';
import GetPosts from '../components/GetPosts.vue'; // 변경된 컴포넌트 이름
import BoardComponent from '../components/BoardComponent.vue'; // 새 컴포넌트 추가
import TextEncodeComponent from '../components/TextEncodeComponent.vue';
import DefaultView from '../components/DefaultView.vue'; // 새 컴포넌트 추가


const routes = [
  {
    path: '/ExampleComponent',
    name: 'ExampleComponent',
    component: ExampleComponent,
  },
  {
    path: '/constrainedframe',
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
          {
            path: 'encode',
            name: 'TextEncodeComponent',
            component: TextEncodeComponent,
          },
        ],
  },
  {
    path: '/defaultview',
    name: 'DefaultView',
    component: DefaultView,
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
