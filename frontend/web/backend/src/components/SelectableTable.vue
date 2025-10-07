<template>
  <div class="selectable-table">
    <table>
      <thead>
        <tr>
            <th>ID</th>
          <th v-for="col in columns" :key="col">{{ props.columnsMapping[col] }}</th>
        </tr>
      </thead>
      <tbody>
        <tr 
          v-for="(item, index) in items" 
          :key="props.idAccessor ? props.idAccessor(item) : index"
          :class="{ selected: selectedIndex === (props.idAccessor ? props.idAccessor(item) : index) }"
          @click="selectRow(props.idAccessor ? props.idAccessor(item) : index)"
        >
            <td>{{ index + 1 }}</td>
          <td v-for="col in columns" :key="col">{{ item[col] }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  items: {
    type: Array,
    required: true
  },
  columns: {
    type: Array,
    required: true
  }, 
  columnsMapping: {
    type: Object,
    required: true
  },
  idAccessor: {
    type: Function,
    required: false
  }
});

const emit = defineEmits(["update:modelValue"])

const selectedIndex = ref(null);

function selectRow(index) {
  selectedIndex.value = index;
  emit("update:modelValue", index)
}
</script>

<style scoped>
.selectable-table table {
  width: 95%;
  border-collapse: collapse;
  font-family: Arial, sans-serif;
}

.selectable-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.selectable-table th{
  border: 1px solid #ddd;
  border-bottom: 5px solid #ddd;
  padding: 8px;
  text-align: center;
}


.selectable-table tbody tr:hover {
  background-color: hsla(160, 100%, 32%, 1);
  color: white;
  cursor: pointer;
}

.selectable-table tbody tr.selected {
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
}
</style>
