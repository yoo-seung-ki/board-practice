import { createRouter, createWebHistory } from 'vue-router';
import ExampleComponent from '../components/ExampleComponent.vue';
import ConstrainedFrame from '../components/ConstrainedFrame.vue';

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
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
