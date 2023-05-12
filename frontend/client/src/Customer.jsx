import { Wrap, WrapItem, Spinner, Text, Center } from '@chakra-ui/react';
import SideBarWithHeader from './components/shared/SideBar.jsx';
import {useEffect, useState} from 'react';
import {getCustomers} from './services/client.jsx';
import CardWithImage from './components/Card';
import DrawerForm from './components/DrawerForm.jsx';
import { errorNotification} from "./services/Notification.js";

const Customer = ()  => {
    const [ customers, setCustomers] = useState([]);
    const [ isLoading, setIsLoading] = useState(false);
    const [err, setErr] = useState("");

    const fetchCustomers =  () => {
        setIsLoading(true)
        setTimeout(() => {
            getCustomers().then((res) => {
                setCustomers(res.data);
            }).catch(() => {
                setErr(err.response.data.message);
                errorNotification(
                    err.response.data.title,
                    err.response.data.message
                )
            }).finally(() => {
                setIsLoading(false);
            })
        }, 1000);
    }



    useEffect(() => {
        fetchCustomers();
    }, []);

        if(isLoading){
          return ( <SideBarWithHeader>
              <Center h={"500px"}>
                  <Spinner
                      thickness='4px'
                      size='xl'
                      speed='0.65s'
                      emptyColor='gray.200'
                      color='facebook.500'
                  />
              </Center>

            </SideBarWithHeader>)
        }

    if(err || customers.length <= 0) {
        return (
            <SideBarWithHeader>
                <Center h={"500px"}>

                    <DrawerForm
                        fetchCustomers={fetchCustomers}
                    />
                    <Text fontSize={"4xl"} ml={'30px'}>
                        No Customers Yet.
                    </Text>
                </Center>
            </SideBarWithHeader>
        )
    }

    return (
        <SideBarWithHeader justify={"center"} align={"center"}>
            <DrawerForm
            fetchCustomers={fetchCustomers}
            />
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            fetchCustomers={fetchCustomers}
                            {...customer}
                            imageNumber={index}
                        />
                    </WrapItem>

                ))}
            </Wrap>

        </SideBarWithHeader>
    )
}

export default Customer;
