import {
    Button,
    Checkbox,
    Flex,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Link,
    Stack,
    Image,
    Text,
    Box,
    AlertIcon,
    Alert
} from '@chakra-ui/react';
import {useEffect} from 'react';
import {useAuth} from '../context/AuthContext.jsx';
import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {useNavigate} from 'react-router-dom';
import {successNotification, errorNotification} from "../../services/Notification.js";




const MyTextInput = ({label, ...props}) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};


const LoginForm = () => {
    const {login} = useAuth();
    const navigate = useNavigate();
    return (
        <Formik
            validateOnMount={true}
            validationSchema=
                {Yup.object({
                    username: Yup.string().email("Must be valid email").required("Email is required"),
                    password: Yup.string().min(5, "Password must be at least 5 characters").max(20, "Password cannot be more than 20 characters").required("Password is required")
                })}
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true)
                login(values).then(res => {
                    navigate("/dashboard")
                    successNotification("Login Successful")
                }).catch(err => {
                    errorNotification("Incorrect Password/Username")
                }).finally(() => {
                    setSubmitting(false)
                })
            }}>

            {({isValid, isSubmitting}) => (
                 <Form>
                <Stack spacing='15'>
                    <MyTextInput
                    label={"Email"}
                    name={"username"}
                    type={"email"}
                    placeholder={"hello@customerdata.com"}
                    />
                    <MyTextInput
                        label={"Password"}
                        name={"password"}
                        type={"password"}
                        placeholder={"Type your password"}
                    />

                    <Button
                        type={"submit"}
                        isDisabled={!isValid || isSubmitting}>
                        Login
                    </Button>
                </Stack>
                </Form>
            )}

        </Formik>
    )
}

const Login = () => {

    const {customer} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if(customer) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                    src={
                        'https://clearcode.cc/wp-content/uploads/2019/11/The-anatomy-of-a-customer-data-platform-CDP-main-image.png'
                    }
                    size={"200px"}
                    alt={"Login Image"}
                    />
                    <Heading fontSize={'2xl'} mb='15'>Sign in to your account</Heading>
                    <LoginForm />
                </Stack>
            </Flex>
            <Flex
                flex='1'
                p='4'
                flexDirection='column'
                alignItems='center'
                justifyContent='center'
                bgGradient={[
                    'linear(to-tr, teal.300, yellow.400)',
                    'linear(to-t, blue.200, teal.500)',
                    'linear(to-b, orange.100, purple.300)',
                ]}>

                <Text
                    fontSize={'6xl'}
                    color={'black'}
                    fontWeight={'bold'}
                    mb={5}>
                    <Link href={"https://felixvargasjr.com"}>
                        Enroll Now
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://clearcode.cc/wp-content/uploads/2019/05/The-Anatomy-Of-a-Data-Management-Platform-DMP-infographic-main-image.png'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default Login;