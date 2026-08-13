<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$t('system.profile.nickName')" prop="nickName">
      <el-input v-model="user.nickName" />
    </el-form-item>
    <el-form-item :label="$t('system.profile.phoneNumber')" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item :label="$t('system.profile.email')" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item :label="$t('system.profile.gender')">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">{{ $t('system.profile.male') }}</el-radio>
        <el-radio label="1">{{ $t('system.profile.female') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('system.profile.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('system.profile.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from '@/api/system/user'

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [{ required: true, message: this.$t('system.profile.nickNameRequired'), trigger: 'blur' }],
        email: [
          { required: true, message: this.$t('system.profile.emailRequired'), trigger: 'blur' },
          {
            type: 'email',
            message: this.$t('system.profile.emailInvalid'),
            trigger: ['blur', 'change']
          }
        ],
        phonenumber: [
          { required: true, message: this.$t('system.profile.phoneNumberRequired'), trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: this.$t('system.profile.phoneNumberInvalid'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    submit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.msgSuccess(this.$t('system.profile.modifySuccess'))
          })
        }
      })
    },
    close() {
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: '/index' })
    }
  }
}
</script>
