<template>
  <v-app id="inspire">
       <v-app-bar flat>
         <v-btn @click="$router.push('/ExampleComponent')">Example Component</v-btn>
         <v-btn @click="$router.push('/ConstrainedFrame')">ConstrainedFrame</v-btn>
         <v-btn @click="$router.push('/defaultview')">Default View</v-btn>
         <v-btn @click="goToHome">Home</v-btn>
       </v-app-bar>
    <v-app-bar flat>
      <v-container class="mx-auto d-flex align-center justify-center">
        <v-avatar
          class="me-4"
          color="grey-darken-1"
          size="32"
        ></v-avatar>

        <v-btn
          v-for="link in links"
          :key="link"
          :text="link"
          variant="text"
        ></v-btn>

        <v-spacer></v-spacer>

        <v-responsive max-width="160">
          <v-text-field
            density="compact"
            label="Search"
            rounded="lg"
            variant="solo-filled"
            flat
            hide-details
            single-line
          ></v-text-field>
        </v-responsive>
      </v-container>
    </v-app-bar>

    <v-main class="bg-grey-lighten-3">
      <v-container>
        <v-row>
          <v-col cols="2">
            <v-sheet rounded="lg">
              <v-list rounded="lg">
                <v-list-item
                  v-for="n in 5"
                  :key="n"
                  :title="`List Item ${n}`"
                  link
                  @click="navigateToListItem(n)"
                ></v-list-item>

                <v-divider class="my-2"></v-divider>

                <v-list-item
                  color="grey-lighten-4"
                  title="Refresh"
                  link
                ></v-list-item>
              </v-list>
            </v-sheet>
          </v-col>

          <v-col>
            <v-sheet
              min-height="70vh"
              rounded="lg"
            >
              <router-view></router-view> <!-- 동적으로 컴포넌트를 표시하는 영역 -->
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>


<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const links = ref([
  'Dashboard',
  'Messages',
  'Profile',
  'Updates',
]);

const goToHome = () => {
  router.push('/');
};

const navigateToListItem = (itemNumber) => {
  if (itemNumber === 1) {
    router.push('/constrainedframe/posts');
  } else if (itemNumber === 2) {
    router.push('/constrainedframe/board');
  } else if (itemNumber === 3) {
    router.push('/constrainedframe/encode');
  } else {
    if (router.currentRoute.value.path === '/constrainedframe') {
      router.go(0); // 현재 경로와 동일하면 페이지 새로고침
    } else {
      router.push('/constrainedframe');
    }
  }
};

</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
</style>
