package com.example.how.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.how.R
import com.example.how.databinding.FragmentHomeBinding
import com.example.how.helper.showBottomSheet
import com.example.how.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // Variável que armazena a referência para o objeto de binding do fragmento, permitindo o acesso aos elementos de layout.


    private val binding get() = _binding!!
    // Propriedade para acessar o objeto de binding de forma conveniente.


    private lateinit var auth: FirebaseAuth
    // Objeto que permite autenticar e gerenciar usuários com o Firebase Authentication.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Infla o layout do fragmento usando o objeto inflater e o objeto de binding.

        return binding.root
        // Retorna a raiz da hierarquia de layout inflada, que será exibida como a visualização do fragmento.

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Chamada ao método onViewCreated() da superclasse para realizar qualquer processamento adicional necessário.

        auth = Firebase.auth
        // Inicializa o objeto auth com a instância do Firebase Authentication.


        configTabLayout()
        // Configura o TabLayout para exibir as guias (tabs) e associá-las com o ViewPager.


        initClicks()
        // Inicializa os cliques de eventos para os elementos de layout do fragmento.

    }



    private fun initClicks() {
        // Função responsável por inicializar os cliques de eventos para os elementos de layout.

        binding.ibLogout.setOnClickListener { logoutApp() }
        // Configura um clique de evento para o elemento ibLogout (um botão de logout),
        // para chamar a função logoutApp() quando o botão for clicado.

        binding.ibRemove.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser

            user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showBottomSheet(
                            message = R.string.text_delete_account
                        )

                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.action_homeFragment_to_authentication)

                    } else {
                        showBottomSheet(
                            message = R.string.erro_delete_account
                        )
                    }
                }
            // Lógica para exclusão da conta


            }

        binding.ibEdit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_resetPasswordFragment32)
        }

    }

    private fun logoutApp(){
        // Função responsável por executar o logout do aplicativo.

        auth.signOut()
        // Realiza o logout do usuário atual, chamando o método signOut() do objeto auth.

        findNavController().navigate(R.id.action_homeFragment_to_authentication)
        // Navega para o destino de navegação "action_homeFragment_to_authentication",
        // que provavelmente representa a tela de autenticação do aplicativo.
        // Isso permite que o usuário seja redirecionado para a tela de autenticação após o logout.
    }

    private fun configTabLayout() {
        // Função responsável por configurar o TabLayout e associá-lo com o ViewPager.

        val adapter = ViewPagerAdapter(requireActivity())
        // Cria uma instância do adaptador ViewPagerAdapter, passando a atividade atual como parâmetro.

        binding.viewPager.adapter = adapter
        // Configura o ViewPager com o adaptador recém-criado.


        adapter.addFragment(TodoFragment(), R.string.status_task_todo)
        // Adiciona um fragmento TodoFragment ao adaptador, associando o título "A Fazer".

        adapter.addFragment(DoingFragment(), R.string.status_task_doing)
        // Adiciona um fragmento DoingFragment ao adaptador, associando o título "Fazendo".

        adapter.addFragment(DoneFragment(), R.string.status_task_done)
        // Adiciona um fragmento DoneFragment ao adaptador, associando o título "Feitas".


        binding.viewPager.offscreenPageLimit = adapter.itemCount
        // Define o limite de páginas em cache do ViewPager como a quantidade de fragmentos no adaptador.


        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab, position ->
            // Cria um objeto TabLayoutMediator para associar o TabLayout com o ViewPager.

            tab.text = getString(adapter.getTitle(position))
            // Define o texto de cada guia (tab) do TabLayout com base no título fornecido pelo adaptador.


        }.attach()
        // Finaliza a associação do TabLayout com o ViewPager.


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}