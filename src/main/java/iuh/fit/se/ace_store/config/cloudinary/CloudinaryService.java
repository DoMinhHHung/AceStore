package iuh.fit.se.ace_store.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary();
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        cloudinary.config.cloudName = cloudName;
        cloudinary.config.apiKey = apiKey;
        cloudinary.config.apiSecret = apiSecret;
    }

    public List<String> uploadFiles(List<MultipartFile> files, String type) throws IOException {
        if (files == null || files.isEmpty()) return new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", type.equals("video") ? "video" : "image"));
            urls.add(uploadResult.get("secure_url").toString());
        }
        return urls;
    }
}
