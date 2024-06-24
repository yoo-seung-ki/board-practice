<template>
  <v-app id="inspire">
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

        <v-btn @click="goToHome">Back to Home</v-btn>

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
import { ref,onMounted, onUnmounted, getCurrentInstance  } from 'vue';
import { useRouter } from 'vue-router';

const { emit } = getCurrentInstance();
const router = useRouter();

const goToHome = () => {
  router.push('/');
};

const navigateToListItem = (itemNumber) => {
  if (itemNumber === 1) {
    router.push('/ConstrainedFrame/posts');
  } else if (itemNumber === 2) {
    router.push('/ConstrainedFrame/board');
  }
};

onMounted(() => {
  // 컴포넌트가 마운트될 때 네비게이션 바를 숨김
  emit('toggle-nav', false);
});

onUnmounted(() => {
  // 컴포넌트가 언마운트될 때 네비게이션 바를 다시 표시
  emit('toggle-nav', true);
});

const links = ref([
  'Dashboard',
  'Messages',
  'Profile',
  'Updates',
]);
</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
</style>
